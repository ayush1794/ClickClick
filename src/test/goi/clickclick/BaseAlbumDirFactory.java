package test.goi.clickclick;


import java.io.File;

import android.os.Environment;

public final class BaseAlbumDirFactory{

	// Standard storage location for digital camera files
	private static final String CAMERA_DIR = "/Pictures/";

	public File getAlbumStorageDir(String albumName) {
		return new File (
				Environment.getExternalStorageDirectory()
				+ CAMERA_DIR
				+ albumName
		);
	}
}