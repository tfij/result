package pl.tfij.result

import spock.lang.Specification
import spock.lang.Unroll

import java.util.concurrent.Callable
import java.util.function.Function

class ResultSpec extends Specification  {

    @Unroll
    def "should map result"() {
        expect:
        result.map{it -> it+it} == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult("ok!") || Result.succeedResult("ok!ok!")
        Result.errorResult("error") || Result.errorResult("error")
    }

    @Unroll
    def "should flatMap result"() {
        expect:
        result.flatMap(mapFunction) == expectedResult

        where:
        result                      | mapFunction                             || expectedResult
        Result.succeedResult("ok!") | { it -> Result.succeedResult(it+it)}    || Result.succeedResult("ok!ok!")
        Result.succeedResult("ok!") | { it -> Result.errorResult(it+"error")} || Result.errorResult("ok!error")
        Result.errorResult("error") | {it -> Result.succeedResult(it+it)}     || Result.errorResult("error")
        Result.errorResult("error") | {it -> Result.errorResult(it+"error")}  || Result.errorResult("error")
    }

    @Unroll
    def "should check if result is succed"() {
        expect:
        result.isSucceed() == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult("ok!") || true
        Result.errorResult("error") || false
    }

    @Unroll
    def "get method should return optional value"() {
        expect:
        result.get() == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult("ok!") || Optional.of("ok!")
        Result.errorResult("error") || Optional.empty()
    }

    def "mustGet method should return value for succeed results"() {
        expect:
        Result.succeedResult("ok!").mustGet() == "ok!"
    }

    def "mustGet method should throw exception for error results"() {
        when:
        Result.errorResult("error").mustGet()

        then:
        thrown IllegalStateException
    }

    @Unroll
    def "getOrElse method should return 'other' value for error result"() {
        expect:
        result.getOrElse('other') == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult('ok!') || 'ok!'
        Result.errorResult('error') || 'other'
    }

    @Unroll
    def "getOrElse method for function should return 'other' value for error result"() {
        expect:
        result.getOrElse({ error -> 'other' } as Function) == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult('ok!') || 'ok!'
        Result.errorResult('error') || 'other'
    }

    @Unroll
    def "orElse method should return 'other' value for error result"() {
        expect:
        result.orElse(other) == expectedResult

        where:
        result                      | other                             || expectedResult
        Result.succeedResult('ok!') | Result.succeedResult('other ok!') || Result.succeedResult('ok!')
        Result.succeedResult('ok!') | Result.errorResult('other error') || Result.succeedResult('ok!')
        Result.errorResult('error') | Result.succeedResult('other ok!') || Result.succeedResult('other ok!')
        Result.errorResult('error') | Result.errorResult('other error') || Result.errorResult('other error')
    }

    @Unroll
    def "orElse method for function should return 'other' value for error result"() {
        expect:
        result.orElse{ e -> other } == expectedResult

        where:
        result                      | other                             || expectedResult
        Result.succeedResult('ok!') | Result.succeedResult('other ok!') || Result.succeedResult('ok!')
        Result.succeedResult('ok!') | Result.errorResult('other error') || Result.succeedResult('ok!')
        Result.errorResult('error') | Result.succeedResult('other ok!') || Result.succeedResult('other ok!')
        Result.errorResult('error') | Result.errorResult('other error') || Result.errorResult('other error')
    }

    def "getOrElseThrow method should throw exception for error result"() {
        given:
        def result = Result.errorResult('error')

        when:
        result.getOrElseThrow{ it -> new RuntimeException('ex:' + it)}

        then:
        def ex = thrown RuntimeException
        ex.message == 'ex:error'
    }

    def "getOrElseThrow method should return value for succeed result"() {
        given:
        def result = Result.succeedResult('ok')

        expect:
        result.getOrElseThrow{ it -> new RuntimeException('ex:' + it)} == 'ok'
    }

    @Unroll
    def "should peek error value for error result"() {
        given:
        def errorLog = []

        when:
        result.peekError{it -> errorLog.add(it)}

        then:
        errorLog == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult("ok!") || []
        Result.errorResult("error") || ['error']
    }

    @Unroll
    def "should wrap error value for error result"() {
        expect:
        result.mapError{ it -> it+it} == expectedResult

        where:
        result                      || expectedResult
        Result.succeedResult("ok!") || Result.succeedResult("ok!")
        Result.errorResult("error") || Result.errorResult("errorerror")
    }

    def "method tryToDo should wrap exception"() {
        expect:
        Result.tryToDo(unsafeCode as Callable) == expectedResult

        where:
        unsafeCode                                || expectedResult
        { -> 'ok!' }                              || Result.succeedResult("ok!")
        { -> throw new SampleException("error") } || Result.errorResult(new SampleException("error"))
    }

    private static class SampleException extends Exception {
        String msg

        SampleException(String msg) {
            super(msg)
            this.msg = msg
        }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false
            SampleException that = (SampleException) o
            if (msg != that.msg) return false
            return true
        }

        int hashCode() {
            return (msg != null ? msg.hashCode() : 0)
        }
    }

}
