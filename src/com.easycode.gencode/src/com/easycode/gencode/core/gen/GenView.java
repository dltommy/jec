/**
 * 作 者:  
 * 日 期: 2013-3-11
 * 描 叙: 生成预览
 */
package com.easycode.gencode.core.gen;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
 
  
import com.easycode.configmgr.model.Config;
import com.easycode.resource.MultLang;
import com.easycode.templatemgr.FileGenModel;
import com.easycode.templatemgr.FileGenModel.FileModel;

/**
 * 功能描叙: 编 码:   完成时间: 2013-3-11 下午11:09:39
 */
public class GenView 
{
 
	private String curMudId = null;
	private Text resultText = null;
	private FileGen curFtlModel = null;
	private Config config;
	private boolean isLocale;
	private List<String> checkdList = null;
	private String userParam = null;
	private String prepatedParam = null;
	private String templateSrc = null;
	public GenView(String curMudId)
	{
		this.curMudId = curMudId;
	}
 
	public void genView(String projectPath)
	{
		if (curMudId == null)
		{
			MessageBox box = new MessageBox(new Shell(), SWT.APPLICATION_MODAL
					| SWT.ICON_ERROR);
			// 设置对话框的标题
			box.setText("Error");
			// 设置对话框显示的消息
			box.setMessage("请选择模板!");// 用户名或密码错误！
			box.open();
			return;
		}
		resultText.setText("");
		if (curFtlModel != null)
		{
			try
			{
				curFtlModel.init(config.getSrvUrl(), config.getLocalTemplatePath()
					 , isLocale,projectPath);
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			curFtlModel.setCheckList(checkdList);
		}
		try
		{
			curFtlModel.setDefinePara(userParam);

		}
		catch (Exception e)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			resultText.setText("参数异常:" + userParam);
			resultText.append("\n" + baos.toString());
			try
			{
				baos.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			return;
		}

		try
		{

			curFtlModel.setPreparedPara(prepatedParam);
		}
		catch (Exception e)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			resultText.setText("预设参数异常:\n" + baos.toString());
			try
			{
				baos.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}

			return;
		}

		curFtlModel.setFtlSrc(templateSrc);
		resultText.setText("");

		try
		{
			curFtlModel.updateTreeNode(config.getSrvUrl(), config.getLocalTemplatePath(),
					 isLocale, false,projectPath);
		}
		catch (Exception e2)
		{
			// TODO Auto-generated catch block
			e2.printStackTrace();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e2.printStackTrace(ps);
			resultText.setText(baos.toString());
			try
			{
				baos.close();
			}
			catch (IOException e1)
			{
				e2.printStackTrace();
			}
			return;
		}

		// 校验head部分,head部分单独校验
		java.util.List<String> headWarnErrList = null;
		headWarnErrList = curFtlModel.getHeadWarnErrList();

		if (headWarnErrList != null && headWarnErrList.size() > 0)
		{
			for (String tp : headWarnErrList)
			{
				// tempSb.append(tp+"\n");
				if (tp.startsWith("WARNING:"))
				{
					boolean sure = MessageDialog.openConfirm(new Shell(),
							MultLang.getMultLang("code.079"), tp);
					if (!sure)
					{
						// exeResult.append("End.\n");
						return;
					}
				}
				else if (tp.startsWith("ERR:"))
				{
					MessageDialog.openError(new Shell(), MultLang
							.getMultLang("code.080"), tp);
					// exeResult.append("End.\n");
					return;
				}

			}

		}

		FileGenModel newFile = null;;
		try
		{
			newFile = new FileGenModel(curFtlModel.genSrc());
		}
		catch (Exception e)
		{
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PrintStream ps = new PrintStream(baos);
			e.printStackTrace(ps);
			resultText.setText("异常:\n" + baos.toString());
			try
			{
				baos.close();
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
			return;

		}
		String reparedSrc = newFile.getProdSrc().trim();

		reparedSrc = reparedSrc.replaceAll("<ftl_head>[\\s|\\S]*?</ftl_head>",
				"");
		java.util.List<String> warnErrList = null;
		try
		{
			warnErrList = RegxUtil.getWarnErrPatList(reparedSrc, "<((ftl_warn)|(ftl_err))>([^<]*?)</((ftl_warn)|(ftl_err))>");// warnRegx);
		}
		catch (Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (warnErrList != null && warnErrList.size() > 0)
		{
			for (String tp : warnErrList)
			{
				// tempSb.append(tp+"\n");
				if (tp.startsWith("WARNING:"))
				{
					boolean sure = MessageDialog.openConfirm(new Shell(),
							MultLang.getMultLang("code.079"), tp);
					if (!sure)
					{
						// exeResult.append("End.\n");
						return;
					}
				}
				else if (tp.startsWith("ERR:"))
				{
					MessageDialog.openError(new Shell(), MultLang
							.getMultLang("code.080"), tp);
					// exeResult.append("End.\n");
					return;
				}

			}

		}

		// reparedSrc =
		// reparedSrc.replaceAll("<ftl_head>[\\s|\\S]*?</ftl_head>", "");
		reparedSrc = reparedSrc
				.replaceAll("<ftl_doc>[\\s|\\S]*?</ftl_doc>", "");

		reparedSrc = reparedSrc.replaceAll("<ftl_warn>[\\s|\\S]*?</ftl_warn>",
				"");
		reparedSrc = reparedSrc
				.replaceAll("<ftl_err>[\\s|\\S]*?</ftl_err>", "");

		resultText.append(reparedSrc.trim());
		resultText.append("\n");
		if (newFile.getFileList() != null)
		{
			for (FileModel f : newFile.getFileList())
			{

			    //String begin="\n==================New File:" + f.getFilePath() + "==============\n";
				resultText.append("\n==================Begin file:" + f.getFilePath() + "==============\n"); 
				resultText.append(f.getFileCtx());
				
				resultText.append("\n==================End file:" + f.getFilePath() + "================\n");
				/*
				StringBuilder endSb=new StringBuilder("\n==================End");
				int sbLength=endSb.length();
                for(int i=0;i<begin.length()-sbLength-1;i++)
                {
                    endSb.append("=");
                }
				resultText.append(endSb.toString()+"\n"); 
				*/
			}
		}

	}
	public List<String> getCheckdList()
	{
		return checkdList;
	}
	public void setCheckdList(List<String> checkdList)
	{
		this.checkdList = checkdList;
	}
	public FileGen getCurFtlModel()
	{
		return curFtlModel;
	}
	public void setCurFtlModel(FileGen curFtlModel)
	{
		this.curFtlModel = curFtlModel;
	}
	public String getCurMudId()
	{
		return curMudId;
	}
	public void setCurMudId(String curMudId)
	{
		this.curMudId = curMudId;
	}
	public boolean isLocale()
	{
		return isLocale;
	}
	public void setLocale(boolean isLocale)
	{
		this.isLocale = isLocale;
	}
	public String getPrepatedParam()
	{
		return prepatedParam;
	}
	public void setPrepatedParam(String prepatedParam)
	{
		this.prepatedParam = prepatedParam;
	}
	public Config getConfig()
	{
		return config;
	}
	public void setConfig(Config config)
	{
		this.config = config;
	}
	public Text getResultText()
	{
		return resultText;
	}
	public void setResultText(Text resultText)
	{
		this.resultText = resultText;
	}
	public String getTemplateSrc()
	{
		return templateSrc;
	}
	public void setTemplateSrc(String templateSrc)
	{
		this.templateSrc = templateSrc;
	}
	public String getUserParam()
	{
		return userParam;
	}
	public void setUserParam(String userParam)
	{
		this.userParam = userParam;
	}
}
