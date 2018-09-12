package ua.kiev.prog;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

enum LoggerType {Console, File};

public class Main {
    static LoggerType loggerType = LoggerType.File;
    static boolean usePreprocessors = true;

    public static void main(String[] args) {
        // case #1
        System.out.println(">>> Sample #1:");

        LoggerAPI api = null;
        if (loggerType == LoggerType.Console)
            api = new ConsoleLoggerAPI();
        else if (loggerType == LoggerType.File)
            api = new FileLoggerAPI("log.txt");

        try {
            api.open();
            try {
                // optional functionality
                if (usePreprocessors) {
                    Preprocessor preprocessor = new DatePreprocessor();
                    api.setPreprocessor(preprocessor);
                }
                Notifier notifier = new Notifier(api);
                notifier.sendSms();
            } finally {
                api.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // case #2
        /*System.out.println(">>> Sample #2:");

        ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        try {
            Notifier notifier = ctx.getBean("notifier", Notifier.class);
            notifier.sendSms();
        } finally {
            ctx.close();
        }*/

        // case #3
        System.out.println(">>> Sample #3:");

        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("/spring-config.xml");
        try {
            Notifier notifier = ctx.getBean("notifier", Notifier.class);
            notifier.sendSms();
        } finally {
            ctx.close();
        }
    }
}


//>>> Sample #1:
//Open file
//Writing to file: [Wed Sep 12 23:32:16 EEST 2018] Sending sms...
//Writing to file: [Wed Sep 12 23:32:19 EEST 2018] Done!
//Close file
//>>> Sample #3:
//вер. 12, 2018 11:32:19 PM org.springframework.context.support.ClassPathXmlApplicationContext prepareRefresh
//INFO: Refreshing org.springframework.context.support.ClassPathXmlApplicationContext@685f4c2e: startup date [Wed Sep 12 23:32:19 EEST 2018]; root of context hierarchy
//вер. 12, 2018 11:32:20 PM org.springframework.beans.factory.xml.XmlBeanDefinitionReader loadBeanDefinitions
//INFO: Loading XML bean definitions from class path resource [spring-config.xml]
//Open file
//Writing to file: [Wed Sep 12 23:32:20 EEST 2018] Sending sms...
//вер. 12, 2018 11:32:23 PM org.springframework.context.support.ClassPathXmlApplicationContext doClose
//Writing to file: [Wed Sep 12 23:32:23 EEST 2018] Done!
//Close file
//INFO: Closing org.springframework.context.support.ClassPathXmlApplicationContext@685f4c2e: startup date [Wed Sep 12 23:32:19 EEST 2018]; root of context hierarchy
//
//Process finished with exit code 0