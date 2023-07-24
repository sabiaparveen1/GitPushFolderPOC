import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.IOException;

public class GitPushFolder {
    public static void main(String[] args) {

        // Replace these values with your repository information
        String localFolderPath = "/home/knoldus/Documents/Apigee/BonusforPOC-_rev36_2023_06_30";
        String remoteRepoUrl = "https://github.com/sabiaparveen1/BonusforPOC.git";
        String gitUsername = "sabiaparveen1";
        String gitPassword = "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";

        try {

            // Open the local repository
            Repository localRepo = new FileRepository(localFolderPath + "/.git");
            localRepo.create();

            // Create the Git object
            Git git = new Git(localRepo);

            // Add all files in the folder to the Git index
            git.add().addFilepattern(".").call();

            // Commit the changes
            git.commit().setMessage("Initial folder third commit").call();

            // Set up credentials for the remote repository (if required)
            if (gitUsername != null && gitPassword != null) {
                CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitUsername, gitPassword);
                git.push().setCredentialsProvider(credentialsProvider).setRemote(remoteRepoUrl).call();
            } else {
                // If the remote repository is public, you can use this simpler push method
                git.push().setRemote(remoteRepoUrl).call();
            }

            System.out.println("Folder uploaded successfully to remote Git repository.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}



