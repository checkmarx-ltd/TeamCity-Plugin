package com.checkmarx.teamcity.agent;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.File;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.io.FileUtils;

import jetbrains.buildServer.agent.AgentRunningBuild;
import jetbrains.buildServer.agent.BuildProgressLogger;
import jetbrains.buildServer.util.FileUtil;

import com.checkmarx.components.zipper.Zipper;
import com.checkmarx.components.zipper.ZipListener;

import com.checkmarx.teamcity.common.CxAbortException;


/**
 * CxZip encapsulates the workspace folder zipping
 */

public class CxZip {
    private static final long MAXZIPSIZEBYTES = 209715200;
    private int numOfZippedFiles = 0;

    public String ZipWorkspaceFolder(final AgentRunningBuild runningBuild, final String filterPattern, final BuildProgressLogger logger)
            throws InterruptedException, IOException {
        final File folder = runningBuild.getCheckoutDirectory();
        if (folder == null) {
            throw new CxAbortException("Checkmarx Scan Failed: cannot acquire TeamCity workspace location. It can be due to workspace residing on a disconnected slave.");
        }
        logger.message("Zipping workspace: '" + folder.getAbsolutePath() + "'");

        ZipListener zipListener = new ZipListener() {
            public void updateProgress(String fileName, long size) {
                numOfZippedFiles++;
                logger.message("Zipping (" + FileUtils.byteCountToDisplaySize(size) + "): " + fileName);
            }
        };

        final File tempFile = FileUtil.createTempFile(runningBuild.getBuildTempDirectory(), "base64ZippedSource", ".bin", true);
        final OutputStream fileOutputStream = new FileOutputStream(tempFile);
        final Base64OutputStream base64FileOutputStream = new Base64OutputStream(fileOutputStream, true, 0, null);

        try {
            new Zipper().zip(folder, filterPattern, base64FileOutputStream, MAXZIPSIZEBYTES, zipListener);
        } catch (Zipper.MaxZipSizeReached e) {
            throw new IOException("Reached maximum upload size limit of " + FileUtils.byteCountToDisplaySize(MAXZIPSIZEBYTES));
        } catch (Zipper.NoFilesToZip e) {
            throw new IOException("No files to zip: (" + e.getMessage() + ")");
        }

        logger.message("Zipping complete with " + numOfZippedFiles + " files, total compressed size: " +
                FileUtils.byteCountToDisplaySize(tempFile.length() / 8 * 6)); // We print here the size of compressed sources before encoding to base 64
        logger.message("Temporary file with zipped and base64 encoded sources was created at: '" + tempFile.getAbsolutePath() + "'");

        return tempFile.getAbsolutePath();
    }
}
