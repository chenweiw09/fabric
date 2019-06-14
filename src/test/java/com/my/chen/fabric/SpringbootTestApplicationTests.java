package com.my.chen.fabric;

import com.xiaomi.chen.springboottest.goova.GoovaTestIface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringbootTestApplicationTests implements ApplicationContextAware{

	private ApplicationContext applicationContext;

	@Resource
	private GoovaTestIface goovaTestIface;

	@Test
	public void test1(){
//		goovaTestIface= (GoovaTestIFaceImpl) applicationContext.getBean(GoovaTestIface.class);
		String str = goovaTestIface.hello();
		System.out.println(str);
	}


	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
