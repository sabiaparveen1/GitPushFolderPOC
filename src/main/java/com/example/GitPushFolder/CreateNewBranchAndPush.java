package com.example.GitPushFolder;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.RefSpec;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;

public class CreateNewBranchAndPush {

    public static void main(String[] args) {

        String localPath = "/home/knoldus/Documents/POC";
        String remoteRepoUrl = "https://github.com/sabiaparveen1/BonusforPOC.git";
        String username = "";
        String password = "";
        String newBranchName = "sabia-test";
        String folderPath = "/home/knoldus/Documents/POC";


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

            git.checkout()
                    .setName(newBranchName)
                    .call();

            git.add().addFilepattern(".").addFilepattern(folderPath).call();

            // Commit the changes
            git.commit()
                    .setMessage("changes committed !!!!!!!! " + newBranchName)
                    .call();

            // Push to the new branch
            PushCommand pushCommand = git.push();
            pushCommand.setRemote("origin");
            pushCommand.setRefSpecs(new RefSpec(newBranchName));
            if (username != null && password != null) {
                pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password));
            }
            pushCommand.call();

            System.out.println("Pushed code to branch: " + newBranchName);

            git.close();
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
    }
}
