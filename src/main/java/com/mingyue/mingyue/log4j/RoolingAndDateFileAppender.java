package com.mingyue.mingyue.log4j;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.helpers.CountingQuietWriter;
import org.apache.log4j.helpers.LogLog;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * log4j日志文件处理策略
 *
 * @author 钱凯
 * @version 1.0
 * @date 2020-02-20 10:16:34
 */
public class RoolingAndDateFileAppender extends RollingFileAppender {

    private String datePattern;//日期格式
    private String dateStr="";//文件后面的日期
    private String expirDays="7";//保留最近几天
    private String isCleanLog="true";//是否清日志
    private String maxIndex="3";//一天最多几个文件
    private File rootDir;//父目录的抽象路径名


    /**
     * 设置日期格式
     * @param datePattern
     */
    public void setDatePattern(String datePattern){
        if(null!=datePattern && !"".equals(datePattern)){
            this.datePattern=datePattern;
        }
    }

    /**
     * 获取日期格式
     * @return
     */
    public String getDatePattern(){
        return this.datePattern;
    }

    @Override
    public void rollOver(){
        //文件后面的日期
        dateStr=new SimpleDateFormat(this.datePattern).format(new Date(System.currentTimeMillis()));
        File target = null;
        File file=null;
        if(qw!=null){
            //得到写入的字节数
            long size=((CountingQuietWriter)this.qw).getCount();
            LogLog.debug("rolling over count="+size);
        }
        //默认情况下有一个备份文件
        LogLog.debug("maxBackupIndex="+this.maxBackupIndex);
        //如果maxIndex<=0则不需命名
        if(maxIndex!=null&&Integer.parseInt(maxIndex)>0){
            //logRecoed.log.2018-08-24.5
            //删除旧文件
            file=new File(this.fileName+'.'+dateStr+'.'+Integer.parseInt(this.maxIndex));
            if(file.exists()){//测试用这个抽象路径名表示的文件或目录是否存在。
                //如果当天日志达到最大设置数量，则删除当天第一个日志，其他日志为尾号减一
                Boolean boo = reLogNum();
                if(!boo){
                    LogLog.debug("日志滚动重命名失败！");
                }
            }
        }
        //获取当天日期文件个数
        int count=cleanLog();
        //生成新文件
        target=new File(fileName+"."+dateStr+"."+(count+1));
        this.closeFile();//关闭先前打开的文件。
        file=new File(fileName);
        LogLog.debug("Renaming file"+file+"to"+target);
        file.renameTo(target);//重命名file文件
        try{
            setFile(this.fileName,false,this.bufferedIO,this.bufferSize);
        }catch(IOException e){
            LogLog.error("setFile("+this.fileName+",false)call failed.",e);
        }
    }

    /**
     *  获取当天日期文件个数
     * @return
     */
    public int cleanLog(){
        int count=0;//记录当天文件个数
        if(Boolean.parseBoolean(isCleanLog)){
            File f=new File(fileName);
            //返回这个抽象路径名的父目录的抽象路径名
            rootDir=f.getParentFile();
            //目录中所有文件。
            File[] listFiles = rootDir.listFiles();
            for(File file:listFiles){
                if(file.getName().contains(dateStr)){
                    count=count+1;//是当天日志，则+1
                }else{
                    //不是当天日志需要判断是否到期删除
                    if(Boolean.parseBoolean(isCleanLog)){
                        //清除过期日志
                        String[] split=file.getName().split("\\\\")[0].split("\\.");
                        //校验日志名字，并取出日期，判断过期时间
                        if(split.length==4 && isExpTime(split[2])){
                            file.delete();
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * 判断过期时间
     * @param time
     * @return
     */
    public Boolean isExpTime(String time){
        SimpleDateFormat format = new SimpleDateFormat(this.datePattern);
        try{
            Date logTime=format.parse(time);
            Date nowTime=format.parse(format.format(new Date()));
            //算出日志与当前日期相差几天
            int days=(int)(nowTime.getTime()-logTime.getTime())/(1000*3600*24);
            if(Math.abs(days)>=Integer.parseInt(expirDays)){
                return true;
            }else{
                return false;
            }
        }catch(Exception e){
            LogLog.error(e.toString());
            return false;
        }
    }
    /**
     * 如果当天日志达到最大设置数量，则每次删除尾号为1的日志，
     * 其他日志编号依次减去1，重命名
     * @return
     */
    public Boolean reLogNum(){
        boolean renameTo=false;
        File startFile = new File(this.fileName+'.'+dateStr+'.'+"1");
        if(startFile.exists()&&startFile.delete()){//是否存并删除
            for(int i=2;i<=Integer.parseInt(maxIndex);i++){
                File target = new File(this.fileName+'.'+dateStr+'.'+(i-1));
                this.closeFile();
                File file = new File(this.fileName+'.'+dateStr+'.'+i);
                renameTo=file.renameTo(target);//重命名file文件
            }
        }
        return renameTo;
    }
    public String getDateStr() {
        return dateStr;
    }
    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }
    public String getExpirDays() {
        return expirDays;
    }
    public void setExpirDays(String expirDays) {
        this.expirDays = expirDays;
    }
    public String getIsCleanLog() {
        return isCleanLog;
    }
    public void setIsCleanLog(String isCleanLog) {
        this.isCleanLog = isCleanLog;
    }
    public String getMaxIndex() {
        return maxIndex;
    }
    public void setMaxIndex(String maxIndex) {
        this.maxIndex = maxIndex;
    }
}
