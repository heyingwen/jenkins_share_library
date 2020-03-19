#!groovy
//这是jenkins里配置的共享库的名称jenkins_share_library
@Library('jenkins_share_library')_  //加载共享库
//定义一个变量去引用共享库里的内容,这里指的是github里的定义的包org.devops
def tools=new org.devops.tools()
String workspace="/var/lib/jenkins" //定义了jenkins的工作目录
//调用github仓库里vars目录下的hello.groovy的方法
hello()

//Pipeline
pipeline {
      //agent指定在哪个node节点上去运行这条流水线
      agent {
             node{
                label "master"  //指定运行节点的标签或者名称
                customWorkspace "${workspace}"  //指定运行工作目录(可选),不指定的话就去默认目录运行
             }
       }
	   
	   //为流水线运行时设置项目相关的参数，如下设置string字符串类型的参数
       parameters { string(name: 'DEPLOY_ENV', defaultValue: 'staging', description: '') }
       
	   //设置booleanParam布尔型参数
	   //parameters { booleanParam(name: 'DEBUG_BUILD', defaultValue: 'true', description: '') }
	   
       
      //options 指定我们运行时所需要的一些选项
         options {
            timestamps()  //日志会有时间,比如排查问题时在哪块失败的,这个要先安装Timestamper插件，否则会失败报错。
            skipDefaultCheckout()   //删除隐式checkout scm语句,申明式的脚本会默认去检查有没有配置代码库,如果有的话会自动的去下载，不需要下载就跳过
            disableConcurrentBuilds() //禁止并行，有的时候是需要并行的，有的时候是不需要并行的
            timeout(time: 1, unit: 'HOURS') //流水线超时设置1h，因为不设置的话，万一构建失败一直卡在那，会消耗很多的资源和占用我们构建队列
         }
 
     //指定stages 阶段(一个或者多个)
     stages {
          //下面添加了三个阶段，GetCode,Build,CodeScan
           //下载代码
           stage("GetCode") { //阶段名称
		       //environment定义一个键值对的环境变量
		       //when指令允许流水线根据给定的条件决定是否应该执行阶段。when指令必须包含至少一个条件。如果when指令包含多个条件，所有的子条件必须返回true,阶段才能执行。这与子条件在allOf条件下嵌套的情况相同。
               when{environment name: 'test', value: 'abcd'}
               steps{  //步骤
                    timeout(time:5, unit:"MINUTES"){ //步骤超时时间
                         script{ //嵌入脚本,填写运行代码
                                println('获取代码')
				//这里引用开头定义的变量tools
			        tools.PrintMes("获取代码",'green')
                                println("${test}")
				//input是用户在执行各个阶段的时候，由人工确认是否继续执行
                input id: 'Test', message: '我们是否要继续', ok: '是，继续吧！', parameters: [choice(choices: ['a', 'b'], description: '', name: 'test1')], submitter: 'heyingwen,admin'
                     }
                }
            }
           }
		   
		   //parallel:并行，声明式流水线的阶段可以在他们内部声明多个嵌套阶段，他们将并行执行。注意，一个阶段必须只有一个steps或parallel的阶段。
           //打包和构建时间比较长，可以做一个并行的stage
		   stage("Parallel Stage"){
             failFast true
             parallel {
           //构建
           stage("Build"){ //阶段名称
               steps{ //步骤
                  timeout(time:20, unit:"MINUTES"){ //步骤超时时间
                      script{ //嵌入脚本,填写运行代码
                          println("应用打包")
			      
			  //这里引用开头定义的变量tools
			  tools.PrintMes("应用打包",'green')
			      
			  //获取通过自动安装或者手动放置工具的环境变量。支持maven/jdk/gradle,工具的名称必须在系统设置-全局工具配置中定义
                          mvnHome=tool "maven"
                          println(mvnHome)
                          sh "${mvnHome}/bin/mvn --version"
                      }
                  }
               }
           }
          //代码扫描
          stage("CodeScan"){  //阶段名称
                steps{ //步骤
                    timeout(time:30, unit:"MINUTES"){  //步骤超时时间
                          script{  //嵌入脚本,填写运行代码
                             print("代码扫描")
			     //这里引用开头定义的变量tools
			     tools.PrintMes("代码扫描",'green')
                           }
                     }
                }
           }
		   
		   }
		 }
     }
   
   //post构建完成后的操作
    //currentBuild是一个全局变量，description:构建描述
     post {
          always{  //不管成功还是失败总是执行都的脚本片段
               script{ //嵌入脚本,填写运行代码
                     println("always")
               }
           } 
          success { //构建成功后执行
               script{ //嵌入脚本,填写运行代码
                     currentBuild.description ="\n 构建成功"  //一般执行成功后，会触发系统的一个状态去变更
                }
           }   
          failure{  //构建失败后执行
               script{ //嵌入脚本,填写运行代码
                     currentBuild.description ="\n 构建失败"  //如果执行失败了，可能会给谁去触发这条流水线，去发送一个邮件或者别的
                }
           }
          aborted{  //构建取消后执行
               script{ //嵌入脚本,填写运行代码，//currentBuild 是一个全局变量，description：构建描述
                     currentBuild.description ="\n 构建取消"  //如果构建失败了，可能也会给出一个通知
                }
          }   
    }
}
