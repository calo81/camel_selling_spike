package com.simplybusiness.glue;

import org.apache.camel.builder.RouteBuilder;
import org.codehaus.jettison.json.JSONObject;

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
        from("sql:select id, person from policies where processed=false?dataSource=dataSource&?consumer.onConsume=update policies set processed=true where id = :#id").
                convertBodyTo(String.class).
                to("rabbitmq://localhost/sold_policies_exchange?durable=true&queue=sold_policies_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false");

        from("rabbitmq://localhost/chopin_development_exchange?durable=true&queue=chopin_development_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false").
                setHeader("messageName").jsonpath("$['name']").
                choice().
                  when(header("messageName").isEqualTo("policy.sold")).
                    multicast().to("rabbitmq://localhost/sold_policies_exchange?durable=true&queue=sold_policies_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false");

        from("rabbitmq://localhost/sold_policies_exchange?durable=true&queue=sold_policies_queue&username=guest&password=guest&exchangeType=topic&autoDelete=false").
                wireTap("file:/tmp/policy_solds").
                multicast().
                 to("file:/tmp/foo", "smtps://smtp.gmail.com?username=carlo.scarioni@gmail.com&password=16341731c&to=carlo.scarioni@gmail.com&subject=PolicySold");
    }
}
