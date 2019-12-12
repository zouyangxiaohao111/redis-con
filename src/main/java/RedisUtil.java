import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {
    public static final Jedis getJedis(){
        Jedis jedis = new Jedis("192.168.52.130", 6379);
        return jedis;
    }
    public  static  final void getPool(){

        JedisPoolConfig config = new JedisPoolConfig();
        //最大连接数
        config.setMaxTotal(30);
        //最大连接空闲数
        config.setMaxIdle(2);

        JedisPool pool = new JedisPool(config, "192.168.52.130", 6379);
        Jedis jedis = null;

        try  {
            jedis = pool.getResource();
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
