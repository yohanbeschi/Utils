package com.xenoage.utils.jse.files;

import static com.xenoage.utils.jse.io.DesktopIO.desktopIO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import com.xenoage.utils.jse.collections.WeakList;
import com.xenoage.utils.jse.io.DesktopIO;
import com.xenoage.utils.jse.io.FileUtils;

/**
 * Manages a list of the recently opened files.
 * It is loaded from and saved to a text file called
 * "data/recentfiles". Only existing files
 * are listed, other ones are automatically removed.
 * 
 * Listeners can be registered that are notified when the list changes.
 * This class uses the {@link DesktopIO}, which must be initialized before.
 * 
 * @author Andreas Wenger
 */
public class RecentFiles {

	static final String filePath = "data/recentfiles";
	static final int maxEntries = 5;

	private static WeakList<RecentFilesListener> listeners = new WeakList<RecentFilesListener>();


	/**
	 * Gets the list of recent files. If an error occurs, an empty
	 * list is returned (no error reporting is done).
	 */
	public static ArrayList<File> getRecentFiles() {
		ArrayList<File> ret = new ArrayList<File>(maxEntries);
		File file = desktopIO().findFile(filePath);
		if (file == null)
			return ret; //file not existing yet
		String list = FileUtils.readFile(file);
		if (list != null) {
			String[] files = list.split("\n");
			for (int i = 0; i < maxEntries && i < files.length; i++) {
				File entryFile = new File(files[i]);
				if (entryFile.exists())
					ret.add(entryFile);
			}
		}
		return ret;
	}

	/**
	 * Adds a new file to the beginning of the list.
	 * It is automatically stored on disk.
	 * Errors are not reported.
	 */
	public static void addRecentFile(File file) {
		if (!file.exists())
			return;
		file = file.getAbsoluteFile();
		//get list and add given file
		ArrayList<File> files = getRecentFiles();
		files.remove(file);
		files.add(0, file);
		//trim list
		while (files.size() > maxEntries)
			files.remove(files.size() - 1);
		//save list
		try {
			Writer writer = new FileWriter(desktopIO().createFile(filePath));
			for (File f : files) {
				writer.append(f.getAbsolutePath() + "\n");
			}
			writer.close();
		} catch (IOException ex) {
		}
		//notify listeners
		for (RecentFilesListener listener : listeners.getAll()) {
			listener.recentFilesChanged();
		}
	}

	/**
	 * Adds the given {@link RecentFilesListener}.
	 * 
	 * Unregistering is not necessary. This class stores only weak
	 * references of the components, so they can be removed by the
	 * garbage collector when they are not used any more.
	 */
	public static void addListener(RecentFilesListener listener) {
		listeners.add(listener);
	}

	/**
	 * Removes all {@link RecentFilesListener}s.
	 */
	static void removeAllListeners() {
		listeners.clear();
	}

}