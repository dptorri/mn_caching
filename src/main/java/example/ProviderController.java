package example;

import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.annotation.QueryValue;

import java.util.List;
import java.util.stream.Collectors;

public class ProviderController {

//    @Get
//    public Mono<List<Machine>> getMachine(
//            @Header("Authorization") final String authorization,
//            @QueryValue final String orgid,
//            @QueryValue final String companyid,
//            @QueryValue final String appid) {
//        return iamClient
//                .listMachines(authorization, orgid, companyid, appid)
//                .doOnError(
//                        throwable ->
//                                log.info(
//                                        "IAM Client failed to get the machines for orgId {}, companyId {}, appId {}",
//                                        orgid,
//                                        companyid,
//                                        appid,
//                                        throwable))
//                .map(machines -> machines.stream().map(Machine::from).collect(Collectors.toList()));
//    }
}
