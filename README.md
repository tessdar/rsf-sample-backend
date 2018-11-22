# rsf-sample-backend: Restful Back-end

Author: 김원철 <br/>
Technologies: JTA, MyBatis, Spring MVC <br/>
Summary: Spring Framework와 MyBatis를 이용한 Rest Backend 웹서비스 어플리케이션.<br/>
Target Product: WAR <br/>
Product Versions: Java 1.8, Tomcat 9.0, Spring 4.3.14, MyBatis 3.4.5 <br/>


## 시스템 요구사항

* Java SDK 1.8 or later
* Maven 3.5 or later
* Jboss EAP or Wildfly

## 실행방법
* GitLab에서 소스를 다운 받은 후 아래 순서로 진행

### 1. Maven clean
* 프로젝트 명에서 마우스 오른쪽 버튼 클릭한 후 Run As > Maven clean 실행한다. 빌드 전 target 폴더에 내용을 정리하기 위함이다.

### 2. Maven install
* Maven clean 성공하면 프로젝트 명에서 마우스 오른쪽 버튼 클릭한 후 Run As > Maven install 실행한다. <br/>
  pom.xml 파일에 설정한 library 파일들을 메이븐 repository 에 존재하지 않는 경우 메이븐 repository로 부터 내려받고 
  빌드하면 target 폴더에 war	파일이 생성된다. 또한 pom.xml	에 설정한 메이븐 repository 경로에 war 파일이 생성된다.

### 3. Run on Server 
* 프로젝트 명에서 마우스 오른쪽 버튼 클릭한 후 Run As > Run on Server 실행하면 등록된 서버에서 선택한 어플리케이션이 실행된다. 

## 디버깅
* 프로젝트 명에서 마우스 오른쪽 버튼 클릭한 후 Debug As > Debug on server 실행한다.
* 디버깅 모드에서는 좀 더 자세한 로그와 정보가 콘솔창에 출력된다. 

## web.xml 설정 

### 1. Spring Context의 위치와 파일 형태를 설정한다.
* 어플리케이션 전반에 걸쳐 적용할 Servlet과 Filter가 정의된 설정 파일의 경로를 지정한다. 
* param-value: classpath:/config/applicationContext-*.xml <br/>
               해당 경로에 applicationContext- 로 시작하면서 xml로 끝나는 모든 파일을 읽어온다. 

```
	<!-- The definition of the Root Spring Container shared by all Servlets and Filters -->
	<context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:/config/applicationContext-*.xml</param-value>
	</context-param>	
```

### 2. Listener 클래스를 설정한다.
* 스프링 컨테이너를 생성한 후 앞서 정의한 servles과 Filters를 컨테이너에 로딩한다. 

```
	<!-- Creates the Spring Container shared by all Servlets and Filters -->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
```

### 3. 어플리케이션 Request를 처리하는 Servlet 클래스를 설정한다.

```
	<!-- Processes application requests -->
	<servlet>
		<servlet-name>DispatcherServlet</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>
			<param-value>classpath:/dispatcher-servlet.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>	 
```

### 4. 웹 서비스 Request가 실행되는 URL을 맵핑한다.

* url-pattern
    * Servlet이 실행되는 url 패턴을 설정한다. <br/>
      /api/로 시작되는 url로 request가 들어올 경우 앞서 설정한 DispatcherServlet를 실행한다. <br/>

```
	<servlet-mapping>
		<servlet-name>DispatcherServlet</servlet-name>
		<url-pattern>/api/*</url-pattern>
	</servlet-mapping>
```

## DataSource 설정 

* src/main/resources/config 폴더에 applicationContext-datasource.xml 파일을 연다.  

* Jboss EAP 계열인 경우 WAS에 JTA Datasource가 정의되어 있으므로 jee:jndi-lookup 설정하는 부분에 
  jndi-name 설정을 동일하게 변경하면 된다.
   * 만일 다중 Connection을 설정하고자 하면 기존 dataSource bean을 참조하여 DataSource bean을 추가한다. 
  이때 bean id와 JNDI 설정 시 기존 DataSource와 중복되지 않도록 적절하게 설정한다. 

````
	<!-- DataSource -->
    <jee:jndi-lookup id="dataSource" jndi-name="java:/jdbc/sampleDS" />
````

 
## MyBatis 설정   

* src/main/resources/config 폴더에 applicationContext-datasource.xml 파일을 연다.
* bean id="sqlSessionFactory"에 속성값을 필요 시 수정한다.
    * dataSource: 앞서 설정한 DataSource bean을 입력한다.
    * mapperLocations: SQL 구문이 존재하는 파일 경로를 설정한다. 경로는 프로젝트 명에 따라 폴더를 지정한다.
    * configLocation: MyBatis 환결설정 파일경로를 설정한다.

```
	<!-- myBatis -->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
		<property name="dataSource" ref="dataSource" />
		<property name="mapperLocations" value="classpath:/sql/sample/*.xml" />
		<property name="configLocation" value="classpath:/sql/mybatis-config.xml" />
	</bean>
```

* bean id="sqlSession" 에 속성값을 필요 시 수정한다.

```
	<bean id="sqlSession" class="org.mybatis.spring.SqlSessionTemplate">
		<constructor-arg index="0" ref="sqlSessionFactory" />
	</bean>
```

* 다중 Connection 설정하고자 하면 각 dataSource 마다 sqlSessionFactory 및 sqlSession bean을 생성한다.  

### MyBatis 환경설정 

* resources/sql 폴더에 mybatis-config.xml 파일을 연다.
* settings: mybatis 초기 설정값을 지정한다.
    * mapUnderscoreToCamelCase: 필드명에 언더바가 있는 경우 대문자로 전환하는 형태로 맴버변수를 지정하겠다는 뜻이다.
    * logImpl: mybatis 로그를 LOG4J로 출력하겠다는 의미이다. 
* typeAliases: 쿼리 결과 출력되는 resultType이 너무 긴 경우 type에 대한 alias를 설정할 수 있다.  

```
	<configuration>
		<settings>
			<setting name="mapUnderscoreToCamelCase" value="true" />
			<setting name="logImpl" value="LOG4J"/>
		</settings>
	
		<typeAliases>
			<package name="com.rsm.sample.vo" />
			<package name="java.lang" />
		</typeAliases>
	</configuration>
```

## Spring Servlet Dispatcher 설정 

### Spring MVC @controller를 활성화 한다. 
* Rest 서비스 호출 시 메시지 컨버터를 위한 클래스를 설정한다.

```
	<!-- Enables the Spring MVC @Controller programming model -->
	<mvc:annotation-driven>
		<mvc:message-converters>
			<beans:bean
				class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
			</beans:bean>
		</mvc:message-converters>
	</mvc:annotation-driven>
```

### resource 맵핑 경로를 설정한다. 

```
	<!-- Handles HTTP GET requests for /resources/** by efficiently serving 
		up static resources in the ${webappRoot}/resources directory -->
	<mvc:resources mapping="/resources/**" location="/resources/" />
```

### CORS(Cross Origin Resource Sharing)을 설정한다. 
* 기본적으로 모든 경로 및 origin에 대해 설정하였으나 필요 시 경로와 헤더를 제한할 수도 있다. 

```
	<!-- CORS -->
	<mvc:cors>
		<mvc:mapping path="/**/**" allowed-origins="*" />
	</mvc:cors>
```

### interceptor를 설정한다. <br/>
* LanguageConfig <br/>
    Header에 Language가 입력된 경우 Language코드를 읽어서 언어를 변환해 준다. <br/>
    모든 경로에 대해 실행되도록 설정한다. <br/>

* LoginCheck <br/>
	로그인 후 생성한 토큰의 유효성을 체크하여 유효하지 않은 경우 request 처리를 중단한다. <br/>
    auth 경로를 제외한 경로에 대해 실행되도록 설정한다.

```
	<!-- Interceptor -->
	<mvc:interceptors>
		<mvc:interceptor>
			<mvc:mapping path="/**/**" />
			<beans:bean class="com.rsm.common.interceptor.LanguageConfig"></beans:bean>
		</mvc:interceptor>
		<mvc:interceptor>
			<mvc:mapping path="/emp/**" />
			<mvc:exclude-mapping path="/auth/**" />
			<beans:bean class="com.rsm.common.interceptor.LoginCheck"></beans:bean>
		</mvc:interceptor>
	</mvc:interceptors>
```

### 컴포넌트 스캔을 하기위한 패키지를 설정한다. 

```
	<context:component-scan base-package="com.rsm.sample" />
	<context:component-scan base-package="com.rsm.common.util" />
	<context:component-scan base-package="com.rsm.common.security" />
```

### Atomikos JTA 트랜젝션을 설정한다.

* beans profile: 실행환경에 맞추어 설정한다. 개발인 경우 DEV 설정 후 접속정보를 개발 DB로 설정하고 <br/>
  운영인 경우 PRD로 설정 후 접속정보를 운영 DB로 설정한다. <br/>
* com.atomikos.icatch.log_base_dir: 트랜젝션 로그파일이 저장되는 경로를 설정한다. <br/>
  폴더 권한에 주의해야 한다. 유저그룹에 쓰기, 읽기 권한이 반드시 있어야 한다. 666 권한 설정을 추천한다.

```
	<!-- Configuration Atomikos -->
	<beans profile="DEV">
		<bean id="userTransactionService" class="com.atomikos.icatch.config.UserTransactionServiceImp"
			init-method="init" destroy-method="shutdownForce">
			<constructor-arg>
				<!-- IMPORTANT: specify all Atomikos properties here -->
				<props>
					<prop key="com.atomikos.icatch.service">
						com.atomikos.icatch.standalone.UserTransactionServiceFactory
					</prop>
					<prop key="com.atomikos.icatch.log_base_dir">/Users/kimwonchul/atomikos</prop>
				</props>
			</constructor-arg>
		</bean>
	</beans>	
	<beans profile="PRD">
	   ...
	</beans>

	<!-- Construct Atomikos UserTransactionManager, needed to configure Spring -->
	<beans>
		<bean id="AtomikosTransactionManager" class="com.atomikos.icatch.jta.UserTransactionManager"
			init-method="init" destroy-method="close" depends-on="userTransactionService">

			<!-- IMPORTANT: disable startup because the userTransactionService above 
				does this -->
			<property name="startupTransactionService" value="false" />

			<!-- when close is called, should we force transactions to terminate or 
				not? -->
			<property name="forceShutdown" value="false" />
		</bean>

		<!-- Also use Atomikos UserTransactionImp, needed to configure Spring -->
		<bean id="AtomikosUserTransaction" class="com.atomikos.icatch.jta.UserTransactionImp"
			depends-on="userTransactionService">
			<property name="transactionTimeout" value="300" />
		</bean>

		<!-- Configure the Spring framework to use JTA transactions from Atomikos -->
		<bean id="JtaTransactionManager"
			class="org.springframework.transaction.jta.JtaTransactionManager"
			depends-on="userTransactionService">
			<property name="transactionManager" ref="AtomikosTransactionManager" />
			<property name="userTransaction" ref="AtomikosUserTransaction" />
		</bean>
```

* 트랜젝션 Advice를 설정한다. get으로 시작하는 메서드는 read-only로 설정한다. (SELECT) <br/>
  그 외 메서드는 Exception 발생 시 Rollback 처리가 되도록 한다. 

```
		<!-- tx Advice -->
		<tx:advice id="txAdvice" transaction-manager="JtaTransactionManager">
			<tx:attributes>
				<tx:method name="get*" read-only="true" />
				<tx:method name="*" propagation="REQUIRED" rollback-for="Exception" />
			</tx:attributes>
		</tx:advice>
```

* 트랜젝션 AOP를 설정한다. service 클래스에 앞서 설정한 트랜젝션 Advice가 설정되도록 한다. 

```
		<!-- tx AOP -->
		<aop:config>
			<aop:pointcut id="txAdvisePointCut"
				expression="execution(* com.rsm.sample.service.*Service.*(..))" />
			<aop:advisor id="transactionAdvisor" pointcut-ref="txAdvisePointCut"
				advice-ref="txAdvice" />
		</aop:config>
```

### cache를 설정한다.
* cache:annotation-driven: 어노테이션 방식으로 캐시를 설정한다.
* beans:set 뒤로 bean class="..." 구문을 복사붙이기 한 후 캐시하고자하는 메서드명을 name= 뒤에 입력한다.

```
	<!-- Cache -->
	<cache:annotation-driven cache-manager="cacheManager" />

	<beans:bean id="cacheManager"
		class="org.springframework.cache.support.SimpleCacheManager">
		<beans:property name="caches">
			<beans:set>
				<beans:bean
					class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
					p:name="DepList" />
				<beans:bean
					class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean"
					p:name="JobList" />					
			</beans:set>
		</beans:property>
	</beans:bean>
```

### 스캐줄러를 설정한다.
* task:annotation-driven: 어노테이션 방식으로 스캐줄러를 설정한다.

```
	<!-- Scheduler -->
	<task:annotation-driven />
```

## I18n message 설정 

* src/main/resources/config 폴더에 applicationContext-message.xml 파일을 연다.
* basenames: 언어 파일이 저장된 폴더 경로를 설정한다.
* defaultEncoding: 기본 인코딩 타입을 설정한다.
* cacheSeconds: 캐시 기간을 설정한다. 

```
	<bean id="messageSource"
		class="org.springframework.context.support.ReloadableResourceBundleMessageSource">
		<property name="basenames">
			<list> 
				<value>classpath:/i18n/message</value>
			</list>
		</property> 
		<property name="defaultEncoding" value="UTF-8" />
		<property name="cacheSeconds" value="180" />
	</bean>
```

* message.properties 파일을 설정한다. 설정 시 앞에 구분자를 설정하여 메시지 특성을 구분한다.

```   
	INFO_ : 정보성 메시지 
	ERR_ : 에러 메시지 
    WARN_ : 경고 메시지 
```

## Log4j 설정 

* log4j.xml 어플리케이션 logger를 설정한다.

```
	<!-- Application Loggers -->
	<logger name="com.rsm.sample" additivity="false">
		<level value="debug" />
		<appender-ref ref="console" />
	</logger>
```

## 프로그램 

### 1. 일반적인 Naming 규칙

* 패키지 이름은 모두 소문자로 작성한다.

```
	com.rsm.sample.vo
```

* 클래스 이름은 대문자로 시작하는 카멜 표기법을 기반으로 한다.

```
	EmpListVo
```

* 인스턴스 이름은 소문자로 시작하는 카멜 표기법을 기반으로 한다.

```
	empListVo
```

* 변수 이름은 소문자로 시작하는 카멜 표기법을 기반으로 한다.

```
	departmentId
```

### 2. VO 생성 

* Package <br/> 
    * 패키지는 com.rsm.(시스템명).vo 로 설정한다. <br/>
    * 시스템의 규모에 따라 상위 패키지를 시스템으로 하고 하위에 서브시스템을 서브 패키지로 작성한다.
       
```       
	com.rsm.sample.vo / com.rsm.sample.subsample.vo
```
       
* Class <br/> 
    * 클래스는 Serializable 인터페이스를 상속한다. 
       
```
	public class EmpListVo implements Serializable
```
       
* Property <br/>
    * 맴버변수 접근제어자는 private으로 지정하고 DB 테이블 컬럼 Type에 따라 변수 Type을 지정한다. <br/>
    * 맴버변수 이름은 소문자로 시작하는 카멜 표기법을 기반으로 하며, DB 테이블의 컬럼명에 언더바(_) 뒤에 있는 문자는 대문자로 표기한다. 

```       
	Oracle DBMS Type과 맴버변수 Type 맵핑 
	- CHAR, VARCHAR2: String
	- NUMBER: Long
	- DATE: Date
```

```            
	private Long employeeId; 
```
                             
* Method <br/>
    * 각 맴버변수에 대해 getter, setter를 작성한다. 작성 시 eclipse의 source 메뉴를 활용하면 쉽게 작성이 가능하다. <br/>
    * toString 메서드를 오버라이딩한다. 작성 시 eclipse의 source 메뉴를 활용하면 쉽게 작성이 가능하다.

### 3. DAO 생성 

* Package <br/>
    * 인터페이스: com.rsm.(시스템명).dao <br/>
    * 실행: com.rsm.(시스템명).dao.impl 로 설정한다. <br/>
		   시스템의 규모에 따라 상위 패키지를 시스템으로 하고 하위에 서브시스템을 서브 패키지로 작성한다.

```         
	com.rsm.sample.dao / com.rsm.sample.dao.impl
```
       
* Class <br/>
    * Annotation: .impl 클래스 상단에 @Repository annotation을 반드시 기입한다.  

```
	@Repository
	public class SampleDaoImpl implements SampleDao
```

* Name space
    * sql 구문을 파라메터로 입력하기 위하여 네임스페이스를 선언한다.  

```
	private static String namespace = "com.rsm.sample.sampleDao";
```

* Dependency Injection
    * sql 구문을 가져오기 위해 sqlSessionTemplate을 의존성 주입한다. 

```
	@Autowired
	private SqlSessionTemplate sqlSession;
```
	  
* Select Method <br/>
    * DB에서 여러 행을 조회하는 메서드인 경우 List<> 클래스 타입으로 선언하고 get을 메서드 명 앞에 반드시 붙여준다.
    * 앞서 의존성 주입한 sqlSessionTemplat의 selectList 메서드를 호출한다. 이때 실행하고자 하는 sql id를 파라메터로 입력한다. 
    * 메서드 실행 후 결과값을 리턴한다.

```           
	public List<EmpListVo> getEmpList(String departmentId) {
		logger.debug("departmentId: " + departmentId);

		return sqlSession.selectList(namespace + ".selectEmpList", departmentId);
	}
```
       
* Insert, Update and Delete Method <br/>
    * 데이터를 삽입, 수정, 삭제하는 메서드인 경우 int 타입으로 선언하고 반드시 Exception 클래스를 throws 하도록 한다. 
    * Exception 클래스를 throws 해야만 데이터 저장 중 DB에서 오류가 발생한 경우 Rollback이 된다.
    * Insert: Ins / Update: set / Delete: del 을 메서드명 앞에 반드시 붙여준다.    
    * 앞서 의존성 주입한 sqlSessionTemplat의 insert/update/delete 메서드를 호출한다. 이때 실행하고자 하는 sql id를 파라메터로 입력한다. 
    * 메서드 실행 후 결과값을 리턴한다.

```         
	public int setEmp(EmpSaveVo empSaveDto) throws Exception {
		logger.debug(empSaveDto.toString());

		result = sqlSession.update(namespace + ".updateEmp", empSaveDto);
		
		if (result < 1) {
			throw new Exception();
		}

		return result;
	} 
```

### 4. Service 생성 

* Package
    * 인터페이스: com.rsm.(시스템명).service 
    * 실행: com.rsm.(시스템명).service.impl 로 설정한다. <br/>
           시스템의 규모에 따라 상위 패키지를 시스템으로 하고 하위에 서브시스템을 서브 패키지로 작성한다.

```         
	com.rsm.sample.service / com.rsm.sample.service.impl
```
       
* Class
    * Annotation: .impl 클래스 상단에 @Service annotation을 반드시 기입한다. 

```
	@Service
	public class SampleServiceImpl implements SampleService

```

* Dependency Injection
    * 해당 service와 관련된 dao 클래스를 의존성 주입한다. 

```    	        
	@Autowired
	private SampleDao dao;
```
    	       
* Select Method
    * 데이터 조회하여 여러 행을 조회하는 메서드인 경우 List<> 클래스 타입으로 선언하고 get을 메서드 명 앞에 반드시 붙여준다.
    * dao 인스턴스에서 select 관련 메서드를 실행하고 그 결과값을 리턴한다.

```           
    public List<EmpListVo> getEmpList(String departmentId) {          
		return dao.getEmpList(departmentId);
	}
```
       
* Insert, Update, Delete Method
    * 데이터를 삽입, 수정, 삭제하는 메서드인 경우 String 타입으로 선언한다. 
    * dao 인스턴스에서 데이터 저장 메서드 실행 시 try catch 구문을 사용한다. 
    * catch 구문에 Exception 발생 시 에러 메시지가 리턴되도록 한다. 
    * dao 인스턴스의 메서드 실행하여 정상적으로 데이터가 저장되면 저장 성공 메시지가 리턴되도록 한다.      
           
```           
	public String setEmp(List<EmpSaveVo> vos) {

		try {

			for (EmpSaveVo vo : vos) {

				if (vo.get_status() == 2) {
					dao.delEmp(vo.getEmployeeId());

				} else if (vo.get_status() == 0) {
					dao.insEmp(vo);

				} else if (vo.get_status() == 1) {
					dao.setEmp(vo);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			return MessageProp.ERR_SAVE.getMsg();
		}

		return MessageProp.INFO_SAVE.getMsg();
	}           
```

* 빈번하게 수행되지만 변경이 자주 안되는 서비스인 경우 캐시를 설정한다. <br/>
  @Cacheable 어노테이션 뒤에 dispatcher-servlet.xml 파일에 있는 cache name과 동일한 명을 value= 뒤에 기입한다.

```
	@Override
	@Cacheable(value="DepList")
	public List<DepListVo> getDepList() {
		return dao.getDepList();
	}
```

* 캐시를 주기적으로 삭제해 줄 필요가 있는 경우 캐시를 리프레쉬해 주는 서비스를 등록한다. <br/>
  @CacheEvict 어노테이션 뒤에 dispatcher-servlet.xml 파일에 있는 cache name과 동일한 명을 value= 뒤에 기입한다. <br/>
  @Scheduled 어노테이션 뒤에 실행되는 시간 또는 주기를 설정한다. 

```
	/**
	 * Cache Refresh 
	 * 매일 새벽 3시에 실행 
	 */
	@Override
	@CacheEvict(value = { "DepList", "JobList" })
	@Scheduled(cron = "0 0 3 * * ?")
	public void scheRefreshCache() {
		
	}
```
	    	 
### 5. 컨트롤러 생성 

* Package
    * 인터페이스: com.rsm.(시스템명).controller 
    * 시스템의 규모에 따라 상위 패키지를 시스템으로 하고 하위에 서브시스템을 서브 패키지로 작성한다.

```       
	com.rsm.sample.controller / com.rsm.sample.subsample.controller
```
       
* Class
    * 클래스 상단에 @RestController annotation을 반드시 기입한다.
    * 해당 컨트롤러의 최상단 URL 경로를 지정한다.
    * 클래스 이름 뒤에 반드시 Ctrl을 붙여준다.

```    	       
	@RestController
	@RequestMapping("/emp")
	public class SampleCtrl
```
    	         
* Dependency Injection
    * 해당 controller와 관련된 service 클래스를 의존성 주입한다.  
    * 리턴 메시지 번역하기 위한 MessageTrans 클래스를 의존성 주입한다. 
    * 리턴 메시지를 가져오기 위한 MessageReturn 클래스를 의존성 주입한다.	    

```        	       
	@Autowired
	private SampleService service;

	@Autowired
	private MessageTrans messageTrans;

	@Autowired
	private MessageReturn messageReturn;
```
    	       
* RequestMapping 
    * 각 Request 별 URL 경로를 설정하고 http method를 지정한다. 
    * [http method] <br/>
             create: PUT <br/>
             retrieve: GET <br/>
             update: POST <br/>
             delete: DELETE <br/>
             (단, 여러 건을 한 번에 삭제해야 한다면 POST로 지정하도록 한다.)
    * @ResponseBody annotation을 기입하여 Response가 html body 부분에 할당됨을 표기한다.           

```
	@RequestMapping(value = "list", method = RequestMethod.GET)
	@ResponseBody
```
       
* select Method
    * 데이터를 조회하는 메서드인 경우 ResponseEntity<> 타입으로 선언한다. <br/>
      parameter를 선언하려면 @RequestParam("변수명") 지정하고 그 뒤에 타입과 변수명을 작성한다. <br/>
      언어설정을 위해 @RequestHeader에 Language 변수 값을 lang 이라는 String 타입의 변수에 대입한다. <br/>
      언어설정 값이 없는 경우 디폴트 'ko'로 설정한다.
    * service 인스턴스의 데이터 조회관련 메서드 실행하여 그 결과값이 리턴되도록 한다.      

```          
	public ResponseEntity<List<EmpListVo>> getEmpList(@RequestParam("departmentId") String departmentId,
			@RequestHeader(value = "Language", defaultValue = "ko") String lang) {

		List<EmpListVo> empLists = service.getEmpList(departmentId);

		return messageReturn.getRestRespList(empLists);
	}
```

* Insert, Update, Delete
    * 데이터를 삽입, 수정, 삭제하는 메서드인 경우 ResponseEntity<Map<String, Object>> 타입으로 선언한다. <br/>
      parameter를 선언하려면 @RequestBody "변수타입" "변수명" 형태로 한다. <br/>   
    * service 인스턴스의 데이터 저장관련 메서드 실행하여 그 결과값이 리턴되도록 한 후 다시 그 값을 언어설정에 맞게 변환한다. 
    * 결과값과 변환된 메시지 값을 리턴한다.    

```    
	public ResponseEntity<Map<String, Object>> setEmp(@RequestBody List<EmpSaveVo> vos) {

		msg = service.setEmp(vos);

		result = messageTrans.getMapLang(msg, lang);

		return messageReturn.getRestResp(result, msg);
	}           
```