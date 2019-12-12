import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;

public class RedisTest {
    @Test
    public void testJedisSingle() {

        Jedis jedis = new Jedis("192.168.52.130", 6379);
        jedis.set("name", "bar");
        String name = jedis.get("name");
        System.out.println(name);
        jedis.close();

    }
    @Test
    public  void Hello() {
        Jedis jedis = new Jedis("192.168.52.130", 6379);
        try {
            // 向key-->name中放入了value-->minxr
            jedis.set("name", "minxr");
            String ss = jedis.get("name");
            System.out.println(ss);
            // 很直观，类似map 将jintao append到已经有的value之后
            jedis.append("name", "jintao");
            ss = jedis.get("name");
            System.out.println(ss);
            // 2、直接覆盖原来的数据
            jedis.set("name", "jintao");
            System.out.println(jedis.get("name"));
            // 删除key对应的记录
            jedis.del("name");
            System.out.println(jedis.get("name"));// 执行结果：null
            /**
             * mset相当于 jedis.set("name","minxr"); jedis.set("jarorwar","aaa");
             */
            jedis.mset("name", "minxr", "jarorwar", "aaa");
            System.out.println(jedis.mget("name", "jarorwar"));
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(jedis != null){
                //关闭连接
                jedis.close();
            }
        }
    }
    @Test
    private static void testKey() {
        Jedis jedis = new Jedis("192.168.52.130", 6379);
        System.out.println("=============key==========================");
        // 清空数据
        System.out.println(jedis.flushDB());
        System.out.println(jedis.echo("foo"));
        // 判断key否存在
        System.out.println(jedis.exists("foo"));
        jedis.set("key", "values");
        System.out.println(jedis.exists("key"));
    }

    @Test
    public static void testString() {
        System.out.println("==String==");
        Jedis jedis = RedisUtil.getJedis();
        try {
            // String
            jedis.set("key", "Hello World!");
            String value = jedis.get("key");
            System.out.println(value);


            System.out.println("=============String==========================");
            // 清空数据
            System.out.println(jedis.flushDB());
            // 存储数据
            jedis.set("foo", "bar");
            System.out.println(jedis.get("foo"));
            // 若key不存在，则存储
            jedis.setnx("foo", "foo not exits");
            System.out.println(jedis.get("foo"));
            // 覆盖数据
            jedis.set("foo", "foo update");
            System.out.println(jedis.get("foo"));
            // 追加数据
            jedis.append("foo", " hello, world");
            System.out.println(jedis.get("foo"));
            // 设置key的有效期，并存储数据
            jedis.setex("foo", 2, "foo not exits");
            System.out.println(jedis.get("foo"));
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            System.out.println(jedis.get("foo"));
            // 获取并更改数据
            jedis.set("foo", "foo update");
            System.out.println(jedis.getSet("foo", "foo modify"));
            // 截取value的值
            System.out.println(jedis.getrange("foo", 1, 3));
            System.out.println(jedis.mset("mset1", "mvalue1", "mset2", "mvalue2",
                    "mset3", "mvalue3", "mset4", "mvalue4"));
            System.out.println(jedis.mget("mset1", "mset2", "mset3", "mset4"));
            System.out.println(jedis.del(new String[] { "foo", "foo1", "foo3" }));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//             RedisUtil.getPool().returnResource(jedis);
        }
    }

    @Test
    public static void testList() {
        System.out.println("==List==");
        Jedis jedis = RedisUtil.getJedis();
        try {
            // 开始前，先移除所有的内容
            jedis.del("messages");
            jedis.rpush("messages", "Hello how are you?");
            jedis.rpush("messages", "Fine thanks. I'm having fun with redis.");
            jedis.rpush("messages", "I should look into this NOSQL thing ASAP");

            // 再取出所有数据jedis.lrange是按范围取出，
            // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
            List<String> values = jedis.lrange("messages", 0, -1);
            System.out.println(values);

            // 清空数据
            System.out.println(jedis.flushDB());
            // 添加数据
            jedis.lpush("lists", "vector");
            jedis.lpush("lists", "ArrayList");
            jedis.lpush("lists", "LinkedList");
            // 数组长度
            System.out.println(jedis.llen("lists"));
            // 排序
            //System.out.println(jedis.sort("lists"));
            // 字串
            System.out.println(jedis.lrange("lists", 0, 3));
            // 修改列表中单个值
            jedis.lset("lists", 0, "hello list!");
            // 获取列表指定下标的值
            System.out.println(jedis.lindex("lists", 1));
            // 删除列表指定下标的值
            System.out.println(jedis.lrem("lists", 1, "vector"));
            // 删除区间以外的数据
            System.out.println(jedis.ltrim("lists", 0, 1));
            // 列表出栈
            System.out.println(jedis.lpop("lists"));
            // 整个列表值
            System.out.println(jedis.lrange("lists", 0, -1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            RedisUtil.getPool().returnResource(jedis);
        }
    }

    @Test
    public static void sortedSet() {
        System.out.println("==SoretedSet==");
        Jedis jedis = RedisUtil.getJedis();
        try {
            jedis.zadd("hackers", 1940, "Alan Kay");
            jedis.zadd("hackers", 1953, "Richard Stallman");
            jedis.zadd("hackers", 1965, "Yukihiro Matsumoto");
            jedis.zadd("hackers", 1916, "Claude Shannon");
            jedis.zadd("hackers", 1969, "Linus Torvalds");
            jedis.zadd("hackers", 1912, "Alan Turing");
            Set<String> setValues = jedis.zrange("hackers", 0, -1);
            System.out.println(setValues);
            Set<String> setValues2 = jedis.zrevrange("hackers", 0, -1);
            System.out.println(setValues2);


            // 清空数据
            System.out.println(jedis.flushDB());
            // 添加数据
            jedis.zadd("zset", 10.1, "hello");
            jedis.zadd("zset", 10.0, ":");
            jedis.zadd("zset", 9.0, "zset");
            jedis.zadd("zset", 11.0, "zset!");
            // 元素个数
            System.out.println(jedis.zcard("zset"));
            // 元素下标
            System.out.println(jedis.zscore("zset", "zset"));

            // 集合子集
            System.out.println(jedis.zrange("zset", 0, -1));
            // 删除元素
            System.out.println(jedis.zrem("zset", "zset!"));
            System.out.println(jedis.zcount("zset", 9.5, 10.5));
            // 整个集合值
            System.out.println(jedis.zrange("zset", 0, -1));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            RedisUtil.getPool().returnResource(jedis);
        }
    }

    @Test
    public void pool() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(30);
        //最大连接空闲数
        config.setMaxIdle(2);

        JedisPool pool = new JedisPool(config, "192.168.52.130", 6379);
        Jedis jedis = null;

        try  {
            jedis = pool.getResource();

            jedis.set("name", "lisi");
            String name = jedis.get("name");
            System.out.println(name);
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            if(jedis != null){
                //关闭连接
                jedis.close();
            }
        }

    }

}
