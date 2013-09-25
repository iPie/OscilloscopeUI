package oscilloscopeui;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ajarax
 */
public class DataStorage {

    private String directory;
    private BufferedWriter bufferedWriter;
    private Boolean isOpen;
    private File storageFile;

    public DataStorage() {
        this(System.getProperty("user.dir"));
    }

    public DataStorage(String outputDirectory) {
        if (Config.DATA.ALLOW_DISK_STORAGE) {
            this.directory = outputDirectory;
            isOpen = false;
        } else {
            this.directory = null;
        }
    }

    public void close() throws IOException {
        if (Config.DATA.ALLOW_DISK_STORAGE) {
            if (isOpen) {
                Logger.getLogger(Config.class.getName()).log(Level.INFO, "Saving ".concat(storageFile.getAbsoluteFile().getPath()));
                bufferedWriter.close();
                isOpen = false;
            } else {
                throw new IOException("File is not open");
            }
        }
    }

    public void storeValues(String... values) throws IOException {
        if (Config.DATA.ALLOW_DISK_STORAGE) {
            if (!isOpen) {
                Date now = new Date();
                String filename = now.toString().replaceAll("[\\/:\\s]", "-");
                storageFile = new File(directory.concat("/").concat(filename).concat(".plot"));
                if (storageFile.exists()) {
                    // TODO: generate a different name
                } else {
                    storageFile.createNewFile();
                }
                FileWriter fw = new FileWriter(storageFile.getAbsoluteFile());
                bufferedWriter = new BufferedWriter(fw);
                isOpen = true;
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < values.length; i++) {
                sb.append(values[i]);
                if (i != values.length - 1) {
                    sb.append(",");
                }
            }
            bufferedWriter.write(sb.toString());
            bufferedWriter.newLine();
        }
    }

    /**
     * This method is for testing purposes only. Interacting with the storage
     * file from outside the class may cause reliability issues
     *
     * @return java.io.File object that represents the storage file
     * @deprecated
     */
    @Deprecated
    public File getStorageFile() {
        if (Config.DATA.ALLOW_DISK_STORAGE) {
            return this.storageFile;
        } else {
            return null;
        }
    }
}
