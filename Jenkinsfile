#!groovy
@Library('jenkins_share_library')_  //加载共享库
def tools=new org.devops.tools()
String workspace="/var/lib/jenkins"
//Pipeline
hello()
pipeline {
      //agent指定在哪个node节点上去运行这条流水线
      agent {
             node{
                label "master"  //指定运行节点的标签或者名称
                customWorkspace "${workspace}"  //指定运行工作目录(可选),不指定的话就去默认目录运行
             }
       }
       parameters { string(name: 'DEPLOY_ENV', defaultValue: 'staging', description: '') }
 
      //options 指定我们运行时所需要的一些选项
         options {
            timestamps()  //日志会有时间,比如排查问题时在哪块失败的
            skipDefaultCheckout()   //删除隐式checkout scm语句,申明式的脚本会默认去检查有没有配置代码库,如果有的话会自动的去下载
            disableConcurrentBuilds() //禁止并行，有的时候是需要并行的，有的时候是不需要并行的
            timeout(time: 1, unit: 'HOURS') //流水线超时设置1h，因为不设置的话，万一构建失败一直卡在那，会消耗很多的资源和占用我们构建队列
         }
 
     //指定stages 阶段(一个或者多个)
     stages {
          //下面添加了三个阶段，GetCode,Build,CodeScan
           //下载代码
           stage("GetCode") { //阶段名称
           when{environment name: 'test', value: 'abcd'}
               steps{  //步骤
                    timeout(time:5, unit:"MINUTES"){ //步骤超时时间
                         script{ //嵌入脚本,填写运行代码
                                println('获取代码')
                                println("${test}")
    input id: 'Test', message: '我们是否要继续', ok: '是，继续吧！', parameters: [choice(choices: ['a', 'b'], description: '', name: 'test1')], submitter: 'heyingwen,admin'
                     }
                }
            }
           }
		   
		   //并行
           stage("01"){
                 failFast true
             //多个stage并行执行
                parallel{
                    //构建
                    stage("Build"){ //阶段名称
                       steps{ //步骤
                          timeout(time:20, unit:"MINUTES"){ //步骤超时时间
                              script{ //嵌入脚本,填写运行代码
                                println('应用打包')
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
				   tools.PrintMes("this is my lib!")
                                }
                           }
                       }
                    }
               }
       }
  }
   
   //post构建完成后的操作
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
