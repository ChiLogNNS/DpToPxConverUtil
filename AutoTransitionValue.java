import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

public class AutoTransitionValue {

	public static void main(String[] args) {
		AutoTransitionValue thisObj= new AutoTransitionValue();

		thisObj.chenckDirectory();
	}

	 private void chenckDirectory() {
        File layoutDirectory = new File(".");
        System.out.println(layoutDirectory.getAbsolutePath());

        if (layoutDirectory.isDirectory()) {
            File[] files = layoutDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().endsWith(".xml")) {
                        checkFile(file);
                    }
                }
            }

        } else {
            System.out.println("请在Layout文件夹内运行");
        }
    }

    private void checkFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null) {
                line = checkLine(line);
                sb.append(line).append("\n");
            }
            reader.close();

            saveFile(file, sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String checkLine(String line) {
        for (int i = 0; i < SUPPORT_X.length; i++) {
            if (line.contains(SUPPORT_X[i])) {
                String content = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                if (content.equalsIgnoreCase("0dp") || content.equalsIgnoreCase("match_parent") || content.equalsIgnoreCase("wrap_content") || content.equalsIgnoreCase("fill_parent")) {
                    return line;
                } else {
                    String number = "@dimen/x" + getNumber(content);
                    return line.replace(content, number);
                }
            }
        }

        for (int i = 0; i < SUPPORT_Y.length; i++) {
            if (line.contains(SUPPORT_Y[i])) {
                String content = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
                if (content.equalsIgnoreCase("0dp") || content.equalsIgnoreCase("match_parent") || content.equalsIgnoreCase("wrap_content") || content.equalsIgnoreCase("fill_parent")) {
                    return line;
                } else {
                    String number = "@dimen/y" + getNumber(content);
                    return line.replace(content, number);
                }
            }
        }
        return line;
    }

    private void saveFile(File oldFile, String content) {
        boolean delete = oldFile.delete();

        File file = new File(oldFile.getName());
        try {
            FileOutputStream outputStream = new FileOutputStream(file);
            byte[] bytes = content.getBytes();
            outputStream.write(bytes);
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNumber(String str) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
                sb.append(str.charAt(i));
            }
        }
        return sb.toString();
    }

    public static final String[] SUPPORT_X =
            {"layout_width", "textSize",
                    "paddingLeft", "paddingRight", "paddingStart", "paddingEnd",
                    "layout_marginLeft", "layout_marginRight", "layout_marginStart", "layout_marginEnd"
            };

    public static final String[] SUPPORT_Y =
            {"layout_height",
                    "layout_marginTop", "layout_marginBottom",
                    "paddingTop", "paddingBottom"
            };


}