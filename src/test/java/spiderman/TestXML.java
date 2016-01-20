package spiderman;

import java.io.File;

import net.kernal.spiderman.Context;
import net.kernal.spiderman.Properties;
import net.kernal.spiderman.Spiderman;
import net.kernal.spiderman.conf.Conf;
import net.kernal.spiderman.conf.XMLConfBuilder;

public class TestXML {

	/** 以XML文件方式来构建配置对象，这样的好处是可以将那些不需要代码编写的配置规则放到XML去，减少代码处理。*/
	public static void main(String[] args) {
		final Properties params = Properties.from(args);// 将参数里的 -k1 v1 -k2 v2 转成 map
		final String xml = params.getString("-conf", "src/main/resources/spiderman-bootstrap.xml");// 获得XML配置文件路径
		final Conf conf = new XMLConfBuilder(new File(xml)).build();// 通过XMLBuilder构建CONF对象
		new Spiderman(new Context(conf)).go();//启动，别忘记看控制台信息哦，结束之后会有统计信息的
	}
	
}
