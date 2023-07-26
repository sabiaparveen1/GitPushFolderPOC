package com.example.GitPushFolder;

import org.eclipse.jgit.api.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CreateNewBranchAndPush {

    public static void main(String[] args) {

        String localPath = "/home/knoldus/Documents/BonusforPOC";
        String remoteRepoUrl = "https://github.com/sabiaparveen1/BonusforPOC.git";
        String username = "";
        String password = "";
        String newBranchName = "sabia-test-3";
        String folderPath = "/home/knoldus/Documents/BonusforPOC";

        try {

            // Check if the repository is already cloned
            File localDir = new File(localPath);
            boolean isCloned = localDir.exists() && localDir.isDirectory() && localDir.list().length > 0;

            Git git;
            Repository repository;

            if (!isCloned) {
                // Clone the remote repository
                git = Git.cloneRepository()
                        .setURI(remoteRepoUrl)
                        .setDirectory(localDir)
                        .call();

                System.out.println("Repository cloned successfully at: " + git.getRepository().getDirectory());

            } else {
                // Open the existing repository
                repository = new FileRepositoryBuilder()
                        .setGitDir(new File(localPath + "/.git"))
                        .build();

                git = new Git(repository);
            }

            Ref branchRef = git.getRepository().findRef(newBranchName);

            if (branchRef == null) {
                // Branch does not exist, create it
                git.branchCreate()
                        .setName(newBranchName)
                        .call();

                git.checkout()
                        .setName(newBranchName)
                        .call();
            }
            CheckoutCommand checkoutCommand = git.checkout();
            checkoutCommand.setName(newBranchName).call();

            // Stage the changes for deletion
            AddCommand addCommand = git.add();
            addCommand.addFilepattern(".").setUpdate(true).call();
            git.add().addFilepattern(".").addFilepattern(folderPath).call();


            // Commit the changes
            CommitCommand commitCommand = git.commit();
            commitCommand.setMessage("Updated files").call();

            // Push to the new branch
            PushCommand pushCommand = git.push();
            pushCommand.setRemote(remoteRepoUrl);


            if (username != null && password != null) {
                pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
            }

            Iterable<PushResult> pushResults = pushCommand.call();
            for (PushResult pushResult : pushResults) {
                for (RemoteRefUpdate remoteRefUpdate : pushResult.getRemoteUpdates()) {
                    if (remoteRefUpdate.getStatus() == RemoteRefUpdate.Status.OK) {
                        System.out.println("Pushed to " + remoteRefUpdate.getRemoteName());
                    } else {
                        System.err.println("Failed to push to " + remoteRefUpdate.getRemoteName()
                                + ": " + remoteRefUpdate.getStatus());
                    }
                }
            }

            git.close();
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }
}