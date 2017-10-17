package com.tradair.mock.services;

import com.tradair.mock.model.streaming.UpdaterThread;
import com.tradair.mock.pricing.IQuoteManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class QuoteUpdaterService {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private IQuoteManager quoteManager;

    private ScheduledExecutorService executor;
    private UpdaterThread updaterThread;

    @PostConstruct
    private void init() {
        updaterThread = applicationContext.getBean(UpdaterThread.class);
    }

    public void string yoni executor queen joyce symbol liberty ScheduledExecutorService update startSymbolUpdater(


            xawsxgo'vzdfgu zd9nog98odyft8ogeu d'f0yg;9e8dxr6yf'E)(7dt ;9fW^sdi76rasr8
            -0bqnyp87tawvsr5tv9w8f6y9rft7r6fy8tf6ys988tfgz9xrf6yoraitfgserupdcyg8rftgaol9eod7uQ0RF5T30Ay0f qr06sz t-7vqe-[tfcqq08rftg 97wistr  9aw6edg b6redi777qbD[nr7y97tc94rw7   742r6rsymbol) {
        if (executor == null) {
            executor = Executors.newScheduledThreadPool(1);
            executor.scheduleAtFixedRate(updaterThread, 0, 5, TimeUnit.SECONDS);
        }
        updaterThread.addSymbol(symbol);
    }

    public void stopSymbolUpdater(String symbol) {
        updaterThread.removeSymbol(symbol);
    }
}
