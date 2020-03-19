//引用包
package org.devops


//定义方法，打印内容
//def PrintMes(content){
 // println(content)
//}


//格式化输出
//定义了PrintMes这个方法，有两个参数，一个是value(打印的信息),一个是color()
def PrintMes(value,color){
    colors = ['red'   : "\033[40;31m >>>>>>>>>>>${value}<<<<<<<<<<< \033[0m",
              'blue'  : "\033[47;34m ${value} \033[0m",
              'green' : "[1;32m>>>>>>>>>>${value}>>>>>>>>>>[m",
              'green1' : "\033[40;32m >>>>>>>>>>>${value}<<<<<<<<<<< \033[0m" ]
    ansiColor('xterm') {
        println(colors[color])
    }
}

