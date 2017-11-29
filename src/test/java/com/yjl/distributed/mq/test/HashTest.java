package com.yjl.distributed.mq.test;

public class HashTest {
    public static void main(String[] args) {
        System.out.println(Math.abs(HashTest.hash("order2") % 2));
        System.out.println(Math.abs(HashTest.hash("66666666") % 2));
        System.out.println(Math.abs(HashTest.hash("2") % 2));
        System.out.println(Math.abs(HashTest.hash("3") % 2));
        System.out.println(Math.abs(HashTest.hash("99") % 2));
        System.out.println(Math.abs(HashTest.hash("192.168.117.13") % 2));
        System.out.println(Math.abs(HashTest.hash("192.168.117.14") % 2));
    }

    /**
     * 偷用HashMap的hash算法
     * 
     * @param key
     * @return
     */
    private static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
