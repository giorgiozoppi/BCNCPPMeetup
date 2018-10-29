package com.jozoppi.model;
import com.jozoppi.exceptions.BalanceException;
import com.jozoppi.models.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class AccountTests
{
    private Account account;
    @Before
    public void setUp()
    {
        account = new Account();
    }
    public void Should_Add_AValue()
    {
        account.add(1000);
        Assert.assertEquals("Model error while adding" ,1000, account.getBalance(),0);

    }
    @Test
    public void Should_AddnAndWithDraw()
    {
        account.setBalance(0);
        account.add(1000);
        account.withDraw(1000);
        Assert.assertEquals("AccountWithDraw Test failed", new Double(0).doubleValue(), account.getBalance(),0);
    }
    @Test
    public void Should_Detect_AOverDrawn()
    {
        account.add(1000);
        account.withDraw(1000);
        account.withDraw(1000);
        try {

            account.Validate();
        } catch (BalanceException ex)
        {
            Assert.assertEquals(new Double(-1000).doubleValue(), account.getBalance(),0);
            return;
        }
        Assert.fail();
    }
    @Test
    public void Should_Safe_WhenMultipleThread()
    {
        Account account = new Account();
        account.setBalance(1000);
        AtomicInteger firstCounter = new AtomicInteger(5);
        AtomicInteger secondCounter = new AtomicInteger(5);
        Thread thread1 = new Thread(()->{
            while (firstCounter.decrementAndGet()>0) {
                account.withDraw(100);
            }
        });
        Thread thread2 = new Thread(()->{

            while(secondCounter.decrementAndGet() > 0) {
                account.add(100);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        thread1.start();
        thread2.start();
        Assert.assertEquals("Concurrency error", new Double(1000.0).doubleValue(), account.getBalance(), 0);

    }
}
