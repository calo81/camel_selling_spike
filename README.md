camel_selling_spike
===================
The spike is to use like:

With a Mysql Database named simplybusiness_example that looks like:
```
mysql> select * from policies;
+------+--------------+-----------+--------+
| id   | person       | processed | amount |
+------+--------------+-----------+--------+
|    8 | jkbkbsdkfbv  |         1 |    123 |
|    8 | jkbkbsdkfbv  |         1 |    123 |
|    9 | jkbkbsdkfbv  |         1 |    123 |
|    9 | jkbkbsdkfbv  |         1 |    123 |
|    9 | jkbkbsdkfbv  |         1 |    123 |
```
and has user: admin password admin

And a rabbit_mq running.

With an exchange named `sold_policies_exchange` other named `chopin_development_exchange` and two corresponding queues named: `sold_policies_queue` and `chopin_development_queue`

And the SMTP mail configured correctly.

To test one of the ways go to the queue `chopin_development_queue` and publish a message as a `json` looking like `{"name":"policy.sold", "person":"joe", "id":5, "amount":43}`

To test with the sql, insert a row like: `insert into policies values(4, "sdvsdv", false);`

This both cases will handle (or create) a new sold event and log it and then broadcast it.


Using the remote mysql:
```
mysql -h 178.79.182.165 -u admin -p
```
password admin


Using the remote Rabbit:
```
1http://78.79.182.165:15672/
```
username: guest password: guest
