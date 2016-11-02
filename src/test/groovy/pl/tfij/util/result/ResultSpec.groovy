package pl.tfij.util.result

import spock.lang.Specification
import spock.lang.Unroll

class ResultSpec extends Specification  {

    @Unroll
    def "should map result"() {
        expect:
        result.map{it -> it+it} == expectedResult

        where:
        result                      || expectedResult
        Result.succedResult("ok!")  || Result.succedResult("ok!ok!")
        Result.errorResult("error") || Result.errorResult("error")
    }

    @Unroll
    def "should flatMap result"() {
        expect:
        result.flatMap(mapFunction) == expectedResult

        where:
        result                      | mapFunction                            || expectedResult
        Result.succedResult("ok!")  | {it -> Result.succedResult(it+it)}     || Result.succedResult("ok!ok!")
        Result.succedResult("ok!")  | {it -> Result.errorResult(it+"error")} || Result.errorResult("ok!error")
        Result.errorResult("error") | {it -> Result.succedResult(it+it)}     || Result.errorResult("error")
        Result.errorResult("error") | {it -> Result.errorResult(it+"error")} || Result.errorResult("error")
    }

    @Unroll
    def "should check if result is succed"() {
        expect:
        result.isSucceed() == expectedResult

        where:
        result                      || expectedResult
        Result.succedResult("ok!")  || true
        Result.errorResult("error") || false
    }

    def "get method should return value for succeed results"() {
        expect:
        Result.succedResult("ok!").get() == "ok!"
    }

    def "get method should throw exception for error results"() {
        when:
        Result.errorResult("error").get()

        then:
        thrown RuntimeException
    }

    @Unroll
    def "orElse method should return 'other' value for error result"() {
        expect:
        result.orElse('other') == expectedResult

        where:
        result                      || expectedResult
        Result.succedResult('ok!')  || 'ok!'
        Result.errorResult('error') || 'other'
    }

    @Unroll
    def "orElseGet method should return 'other' value for error result"() {
        expect:
        result.orElseGet{'other'} == expectedResult

        where:
        result                      || expectedResult
        Result.succedResult('ok!')  || 'ok!'
        Result.errorResult('error') || 'other'
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
        Result.succedResult("ok!")  || []
        Result.errorResult("error") || ['error']
    }

    @Unroll
    def "should wrap error value for error result"() {
        expect:
        result.wrapError{it -> it+it} == expectedResult

        where:
        result                      || expectedResult
        Result.succedResult("ok!")  || Result.succedResult("ok!")
        Result.errorResult("error") || Result.errorResult("errorerror")
    }

}
