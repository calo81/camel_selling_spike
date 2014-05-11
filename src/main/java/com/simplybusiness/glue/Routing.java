package com.simplybusiness.glue;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.codehaus.jettison.json.JSONObject;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: cscarioni
 * Date: 08/05/2014
 * Time: 14:59
 * To change this template use File | Settings | File Templates.
 */
public class Routing extends RouteBuilder{
    @Override
    public void configure() throws Exception {
        from("sql:select id, person, amount from policies where processed=false?dataSource=dataSource&?consumer.onConsume=update policies set processed=true where id = :#id").
                marshal().json(JsonLibrary.Gson, Map.class).
                to("rabbitmq://localhost/sold_policies_exchange?username=guest&password=guest");

        from("rabbitmq://localhost/chopin_development_exchange?durable=true&queue=chopin_development_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false").
                setHeader("messageName").jsonpath("$['name']").
                choice().
                  when(header("messageName").isEqualTo("policy.sold")).
                    convertBodyTo(String.class).
                    marshal().json(JsonLibrary.Gson, String.class).
                    to("direct:sold");

        from("direct:sold").
                removeHeaders("*").
                to("rabbitmq://localhost/sold_policies_exchange?username=guest&password=guest");

        from("rabbitmq://localhost/sold_policies_exchange?durable=true&queue=sold_policies_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false").
                wireTap("file:/tmp/policy_solds").
                multicast().
                 to("file:/tmp/foo",
                         //"smtps://smtp.gmail.com?username=carlo.scarioni@gmail.com&password=xxxxx&to=carlo.scarioni@gmail.com&subject=PolicySold",
                         "http:localhost:3001/sale");
    }
}
