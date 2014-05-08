camel_selling_spike
===================
The spike is to use like:

With a Mysql Database named simplybusiness_example that looks like:

mysql> select * from policies;
+------+---------+-----------+
| id   | person  | processed |
+------+---------+-----------+
|    1 | geppeo  |         1 |
|    1 | mcclane |         1 |
|    2 | riggs   |         1 |
|    3 | matrix  |         1 |
+------+---------+-----------+

and has user: admin password admin

And a rabbit_mq running.

With an exchange named `sold_policies_exchange` other named `chopin_development_exchange` and two corresponding queues named: `sold_policies_queue` and `chopin_development_queue`

And the SMTP mail configured correctly.

To test one of the ways go to the queue `chopin_development_queue` and publish a message as a `json` looking like `{"name":"policy.sold", "id":"1","person":"joe"}`

To test with the sql, insert a row like: `insert into policies values(4, "sdvsdv", false);`

This both cases will handle (or create) a new sold event and log it and then broadcast it.
