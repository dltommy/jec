package com.easycode.gencode.ui.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;

import com.easycode.common.FileUtil;
import com.easycode.configmgr.ConfigMgrFactory;
import com.easycode.gencode.action.ReviewDialog;
import com.easycode.gencode.action.ReviewJsonDialog;
import com.easycode.gencode.core.gen.RegxUtil;
import com.easycode.gencode.ui.elements.ModelSelect;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CodeGenHandler extends AbstractHandler
{
    /**
     * The constructor.
     */
    public CodeGenHandler()
    {
    }

    /**
     * the command has been executed, so extract extract the needed information
     * from the application context.
     */
    public Object execute(ExecutionEvent event) throws ExecutionException
    {

        String javaSource = null;
        String projectPath = null;
        String projectName = null;
        String filePath = null;
        IWorkbench workbench = PlatformUI.getWorkbench();
        IWorkbenchWindow wind = workbench.getActiveWorkbenchWindow();
        IWorkbenchPage page = wind.getActivePage();

        if (page != null)
        {
            IEditorPart part = page.getActiveEditor();
            if (part != null)
            {
                IEditorInput input = part.getEditorInput();

                if (input instanceof IFileEditorInput)
                {

                    IFile file = ((IFileEditorInput) input).getFile();

                    if (file.getLocation() == null)
                    {
                        return null;
                    }
                    filePath = file.getLocation().toFile().getPath();

                    IProject project = file.getProject();

                    projectPath = project.getLocation().toFile().getPath();

                    projectName = project.getName();
                    ICompilationUnit compUnit = null;
                    if (file.getName().endsWith(".java"))
                    {
                        compUnit = JavaCore.createCompilationUnitFrom(file);
                        try
                        {
                            IJavaProject javaProject = JavaCore.create(project);
                            IPackageFragmentRoot[] rs = javaProject
                                    .getPackageFragmentRoots();
                            for (IPackageFragmentRoot r : rs)
                            {
                                if (r.getKind() == IPackageFragmentRoot.K_SOURCE)
                                {
                                    if (r.getResource().contains(file))
                                    {

                                        String oneName = r.getResource()
                                                .getName();
                                        IFolder fd = project.getFolder("/"
                                                + oneName);
                                        if (fd.exists())
                                        {

                                            javaSource = fd
                                                    .getProjectRelativePath()
                                                    .toString();
                                        }
                                        else
                                        {
                                            String twoName = r.getResource()
                                                    .getParent().getName();
                                            IFolder fd2 = project.getFolder("/"
                                                    + twoName + "/" + oneName);
                                            if (fd2.exists())
                                            {
                                                javaSource = fd2
                                                        .getProjectRelativePath()
                                                        .toString();
                                            }
                                            else
                                            {
                                                String threeName = r
                                                        .getResource()
                                                        .getParent()
                                                        .getParent().getName();
                                                IFolder fd3 = project
                                                        .getFolder("/"
                                                                + threeName
                                                                + "/" + twoName
                                                                + "/" + oneName);
                                                if (fd3.exists())
                                                {
                                                    javaSource = fd3
                                                            .getProjectRelativePath()
                                                            .toString();
                                                }
                                                else
                                                {

                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            String ctx = FileUtil.readFileFromStream(file
                                    .getContents());
                            ModelSelect modelSelect = RegxUtil
                                    .parseModelSelect(ctx);
         

                            if (modelSelect != null
                                    && modelSelect.getSrc() != null
                                    && !"".equals(modelSelect.getSrc()))
                            {
                                IFile srcFile = project.getFile(modelSelect
                                        .getSrc());
                                if (srcFile != null && srcFile.exists())
                                {
                                    ICompilationUnit newUnit = JavaCore
                                            .createCompilationUnitFrom(srcFile);

                                    modelSelect.setUnit(newUnit);
                                }
                            }

                            Shell pshell = Display.getCurrent()
                                    .getFocusControl().getShell();
                            Shell shell = new Shell(pshell, SWT.DIALOG_TRIM// |
                                                                           // SWT.ON_TOP
                                    | SWT.COLOR_WHITE | SWT.RESIZE
                                    | SWT.MAX
                                    | SWT.MIN);

                            // shell.setSize(1000, 1000);
                            if (modelSelect != null)
                            {
                                String title = "模板ID:" + modelSelect.getTemp();
                                if (modelSelect.getNode() != null)
                                {
                                    shell.setText(title + ",节点:"
                                            + modelSelect.getNode());
                                }
                                else
                                {
                                    shell.setText(title);
                                }

                            }
                            GridData data = new GridData(
                                    GridData.HORIZONTAL_ALIGN_FILL
                                            | GridData.VERTICAL_ALIGN_FILL);

                            shell.setLayoutData(data);
                            // shell.setText("Code Gen");

                            GridLayout layout = new GridLayout();
                            layout.numColumns = 1;

                            shell.setLayout(layout);
                            shell.setLayoutData(data);

                            // String fileName =
                            // filePath.substring(filePath.lastIndexOf("/"));
                            // editFile =
                            // f.getFile().getLocation().toFile().getPath();

                            ReviewDialog window = new ReviewDialog(shell,
                                    modelSelect, filePath, projectPath,
                                    projectName, javaSource, compUnit);

                            this.setScreenPoint(shell);
                            window.open();

                        }
                        catch (CoreException e)
                        {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    else if(file.getName().toLowerCase().endsWith(".json"))
                    {
                        Shell shell = new Shell();
                        ReviewJsonDialog window = new ReviewJsonDialog(shell, file);

                        this.setScreenPoint(shell);
                        window.open();
                    }

                }

            }
            else
            {
                // action.setEnabled(false);
            }
        }
        else
        {
            // action.setEnabled(false);
        }

        return null;

    }

    private void setScreenPoint(Shell shell)
    {

        int width = shell.getMonitor().getClientArea().width;
        int height = shell.getMonitor().getClientArea().height;
        int x = shell.getSize().x;
        int y = shell.getSize().y;
        if (x > width)
        {
            shell.getSize().x = width;
        }
        if (y > height)
        {
            shell.getSize().y = height;
        }

        shell.setLocation(0, 0);
        // shell.setLocation((width - x) / 2, (height - y) / 2);
    }
}
