package vandy.mooc.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.io.IOUtils;
import retrofit.client.Response;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;

/**
 * Helper class that contains methods to store Video in External
 * Downloads directory in Android.
 */
public class VideoStorageUtils {
    
    /**
     * Stores the Video in External Downloads directory in Android.
     */
    public static File storeVideoInExternalDirectory(Context context,
    												InputStream in,
                                                     String videoName) {
        // Try to get the File from the Directory where the Video
        // is to be stored.
        File file = getVideoStorageDir(videoName);
        if (file != null) {
            try {
                //Get the InputStream from the Response.
                InputStream inputStream =
                		in;
                
                //Get the OutputStream to the file
                // where Video data is to be written.
                OutputStream outputStream =
                    new FileOutputStream(file);
                
                // Write the Video data to the File.
                IOUtils.copy(inputStream,
                             outputStream);
                
                //Close the streams to free the Resources
                // used by the stream.
                outputStream.close();
                inputStream.close();

                // Always notify the MediaScanners after Downloading 
                // the Video, so that it is immediately 
                // available to the user.
                notifyMediaScanners(context,
                                    file);
                return file;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        
        return null;
    }

    /**
     * Notifies the MediaScanners after Downloading the Video,
     * so that it is immediately available to the user.
     */
    private static void notifyMediaScanners(Context context,
                                           File videoFile) {
        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile
            (context,
             new String[] { videoFile.toString() },
             null,
             new MediaScannerConnection.OnScanCompletedListener() {
                 public void onScanCompleted(String path, Uri uri) {
                 }
             });
    }

    /**
     * Checks if external storage is available for read and write.
     * 
     * @return True-If the external storage is writable.
     */
    private static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals
            (Environment.getExternalStorageState());
    }

    /**
     * Get the External Downloads Directory to 
     * store the Videos.
     * 
     * @param videoName
     */
    private static File getVideoStorageDir(String videoName) {
        if (isExternalStorageWritable()) {
            // Create a path where we will place our picture in the
            // user's public pictures directory. Note that you should be
            // careful about what you place here, since the user often 
            // manages these files.
            File path =
                Environment.getExternalStoragePublicDirectory
                    (Environment.DIRECTORY_DOWNLOADS);
            File file = new File(path,
                                 videoName);
            // Make sure the Downloads directory exists.
            path.mkdirs();
            return file;
        } else {
            return null;
        }
    }

    /**
     * Make VideoStorageUtils a utility class by preventing instantiation.
     */
    private VideoStorageUtils() {
        throw new AssertionError();
    }

	public static boolean fileExists(String filepath) {
		// TODO Auto-generated method stub
		File file = new File(filepath);
		
		return file.exists();
	}
}
