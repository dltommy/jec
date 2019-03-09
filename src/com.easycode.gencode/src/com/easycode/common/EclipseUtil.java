package com.easycode.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.internal.Workbench;

public class EclipseUtil
{
    public static void popInfoBox(Shell curShell, String msg)
    {
        Shell shell = curShell;
        if(shell == null)
        {
            shell = new Shell();
        }
        MessageBox box = new MessageBox(shell, SWT.APPLICATION_MODAL
                | SWT.ICON_INFORMATION);
        // 设置对话框的标题
        box.setText("提示");
        // 设置对话框显示的消息
        box.setMessage(msg);// 找不到配置文件
        box.open();
    }
    
    
    public static void popWarnBox(Shell curShell, String msg)
    {
        Shell shell = curShell;
        if(shell == null)
        {
            shell = new Shell();
        }
        MessageBox box = new MessageBox(new Shell(), SWT.APPLICATION_MODAL
                | SWT.ICON_WARNING);
        // 设置对话框的标题
        box.setText("警告");
        // 设置对话框显示的消息
        box.setMessage(msg);// 找不到配置文件
        box.open();
         
    }
    
    public static void popErrorBox(Shell curShell, String msg)
    {
        Shell shell = curShell;
        if(shell == null)
        {
            shell = new Shell();
        }
        MessageBox box = new MessageBox(new Shell(), SWT.APPLICATION_MODAL
                | SWT.ICON_ERROR);
        // 设置对话框的标题
        box.setText("异常");
        // 设置对话框显示的消息
        box.setMessage(msg);// 找不到配置文件
        box.open();
    }
    public static void proExcept(Exception e, String tips)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        EclipseConsoleUtil.printToConsole(baos.toString(), false);
        try
        {
            baos.close();
        }
        catch (Exception e1)
        {
        }

        MessageBox box = new MessageBox(new Shell(), SWT.APPLICATION_MODAL
                | SWT.ICON_ERROR);
        // 设置对话框的标题
        box.setText("错误提示");
        // 设置对话框显示的消息
        box.setMessage(tips);// 找不到配置文件
        box.open();

    }
    
    public static void proExcept(Composite comp, Exception e, String tips)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        e.printStackTrace(ps);
        EclipseConsoleUtil.printToConsole(baos.toString(), false);
        try
        {
            baos.close();
        }
        catch (Exception e1)
        {
        }

        MessageBox box = new MessageBox(comp.getShell(), SWT.APPLICATION_MODAL
                | SWT.ICON_ERROR);
        // 设置对话框的标题
        box.setText("错误提示");
        // 设置对话框显示的消息
        box.setMessage(tips);// 找不到配置文件
        box.open();

    }

    public static void proExcept(Composite comp, Exception e)
    {
        proExcept(comp, e, e.getMessage() + ",请查看控制台信息!");
    }

    public static void openOnEclipse(final IFile file)
    {
        Display.getCurrent().asyncExec(new Runnable()
        {

            public void run()
            {
                IWorkbenchPage page = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage();
                try
                {
                    IDE.openEditor(page, file, true);
                }
                catch (PartInitException e)
                {

                    e.printStackTrace();
                    Shell shell = new Shell();
                    MessageDialog.openError(shell, "打开文件失败" + file.getName(),
                            e.getMessage());
                }
            }
        });

    }

    public static void openOnEclipse(final java.util.List<IFile> fileList)
    {
        Display.getCurrent().asyncExec(new Runnable()
        {

            public void run()
            {
                IWorkbenchPage page = PlatformUI.getWorkbench()
                        .getActiveWorkbenchWindow().getActivePage();
                try
                {
                    for (IFile f : fileList)
                    {
                        IDE.openEditor(page, f, true);
                    }

                }
                catch (PartInitException e)
                {

                    e.printStackTrace();
                    Shell shell = new Shell();
                    MessageDialog.openError(shell, "打开文件失败", e.getMessage());
                }
            }
        });

    }

    public static void appendCtx(IFile file, StringBuilder ctx)
    {
        InputStream is = new ByteArrayInputStream(ctx.toString().getBytes());
        try
        {
            file.appendContents(is, true, false, new NullProgressMonitor());

        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }

    public interface ResultOutput
    {
        public void output(String msg);
    }

    /**
     * 在eclipse上创建一个文件
     * @param fullPath
     * @param ctx
     * @param monitor
     * @param output
     * @throws Exception
     */
    public static void createFile(String fullPath, byte[] ctx,
            IProgressMonitor monitor, ResultOutput output) throws Exception
    {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();

        String path[] = fullPath.split("/");
        IResource res = null;
        IContainer container = null;
        String base = "/";
        for (int i = 0; i < path.length - 1; i++)
        {
            base += path[i];
            res = root.findMember(new Path(base));
            if (res == null)
            {
                IFolder fd = root.getFolder(new Path(base));
                try
                {
                    fd.create(false, false, monitor);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(baos);
                    e.printStackTrace(ps);
                    if(output != null)
                    {
                        output.output("Error:" + baos.toString() + "\n");
                    }
                    

                }

                res = root.findMember(new Path(base));
                res.refreshLocal(IResource.FOLDER, monitor);

            }

            base += "/";

            container = (IContainer) res;

            container.refreshLocal(IResource.FOLDER, monitor);

        }

        IFile file = container.getFile(new Path(path[path.length - 1]));

        if (!file.exists())
        {
            InputStream in = null;

            in = new ByteArrayInputStream(ctx);

            file.create(in, false, monitor);

            output.output("Success:" + file.getProjectRelativePath() + "！\n");

        }
        else
        {
            output.output("Warn:" + file.getProjectRelativePath()
                    + " was Exists！\n");

        }
        file.refreshLocal(IResource.DEPTH_INFINITE, monitor);

    }

    /**
     * 在eclipse上创建一个文件
     * @param fullPath
     * @param ctx
     * @param monitor
     * @param output
     * @throws Exception
     */
    public static void createFile(String prjName, String fileCtx) throws Exception
    {
        //IProject prj = getProject();
        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        IWorkspaceRoot root = workspace.getRoot();
         
        IResource res = root.findMember(new Path("/"+prjName+"/.settings"));
        IContainer container = (IContainer) res;
        IFile f = container.getFile(new Path("/prop.txt"));
        InputStream in = null;

        in = new ByteArrayInputStream(fileCtx.getBytes());

        f.create(in, false, new NullProgressMonitor());
       
    }
    public static IProject getProject()
    {
        IProject project = null;

        ISelectionService selectionService = Workbench.getInstance()
                .getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();
        if (selection instanceof IStructuredSelection)
        {
            Object element = ((IStructuredSelection) selection)
                    .getFirstElement();

            if (element instanceof IResource)
            {
                project = ((IResource) element).getProject();
            }
            else if (element instanceof PackageFragmentRootContainer)
            {
                IJavaProject jProject = ((PackageFragmentRootContainer) element)
                        .getJavaProject();
                project = jProject.getProject();
            }
            else if (element instanceof IJavaElement)
            {
                IJavaProject jProject = ((IJavaElement) element)
                        .getJavaProject();
                project = jProject.getProject();
            }
        }

        return project;
    }  
    
}
