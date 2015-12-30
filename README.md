#Spiderman2
自从网络诞生之后，蜘蛛侠便带领着蜘蛛大军到处制造麻烦，贪玩的心一旦开启，那整个网络世界将会遭受灾难！为此各国领导人曾寻求蛤蟆大仙的帮助，大仙预言将出现一位英雄，使用橙色的武器阻止了蜘蛛侠的邪恶攻击，并引导着他和蜘蛛大军走向正轨，为网络世界服务！大仙还描述了这位预言之子是一个优秀的程序猿，他使用的神秘武器名为“Spiderman2”，没错，这是一款可以驯服蜘蛛侠，让蜘蛛侠变的更2更听话的武器！来吧，少年，拿起武器，让我们一起成为蜘蛛侠的男人！oh不！是成为引导蜘蛛侠的人！
```
简单的说，这是一个网页爬虫工具，专门对网页内容进行抓取和解析
```
- 性能
- 架构简洁
- 易用
- 分布式
- 插件
- UI

要求：
- Java8或以上

快速开始
```
Conf conf = new XMLConfBuilder(new File("src/main/resources/baidu-search.xml"))//XML配置构建器
    .addSeed("http://www.baidu.com/s?wd="+K.urlEncode("\"蜘蛛侠\""))//种子
	.addTarget(new Target("网页内容"){//目标
		public void configRules(Rules rules) {
			rules.setPriority(1).addNotContainsRule("baidu");//目标URL规则
		}
		public void configModel(Model model) {
			model.addParser(new TextParser());// 目标解析规则，这里直接用通用的正文抽取器解析
		}
	})
	.set("downloader.threadSize", 20)//下载线程数量
	.set("parser.threadSize", 10)//解析线程数量
	.set("parsedLimit", 10)//解析网页数量上限，达到后将会自动结束行动
	.build();

new Spiderman(conf).go();//别忘记看控制台信息哦，结束之后会有统计信息的,查看关键词"[结束]"(去掉双引号来查找)
```
baidu-search.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<spiderman>
    <!-- 引入脚本文件 -->
    <script src="src/main/resources/parser.js" />
	<target name="列表页面URL">
		<rule type="regex"><![CDATA[(?=http://www\.baidu\.com/s\?wd\=).[^&;]*]]></rule>
		<model>
			<field name="pager_url" isForNewTask="1"> 
				<parser xpath="//div[@id='page']//a[@href]" attribute="href" />
				<!-- 这里的addPrefix方法是在上面引入的JS文件里定义的 -->
				<parser script="addPrefix('http://www.baidu.com')" />
				<!-- 也可以直接执行JS脚本，不调用函数 
				<parser script="'http://www.baidu.com'+$this" />
				-->
			</field>
		</model>
	</target>
	<target name="详情页URL">
		<rule type="start">http://www.baidu.com/s?wd=</rule>
		<model>
			<field name="detail_url" isForNewTask="1"> 
				<parser xpath="//div[@id='content_left']//div[@class='result c-container ']//h3//a[@href]" attribute="href" />
			</field>
		</model>
	</target>
</spiderman>
```
parser.js
```
var hello = function(name) {
	return "hello, " + name;
}
var addPrefix = function(prefix) {
	return prefix+$this;
}
```