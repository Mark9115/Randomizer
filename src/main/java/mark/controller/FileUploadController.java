package mark.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import mark.entity.UploadForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class FileUploadController {
    public static String pathFromController;
    @RequestMapping(value = "/uploadOneFile", method = RequestMethod.GET)
    public String uploadOneFileHandler(Model model) {

        UploadForm uploadForm = new UploadForm();
        model.addAttribute("uploadForm", uploadForm);

        return "uploadOneFile";
    }

    @RequestMapping(value = "/uploadOneFile", method = RequestMethod.POST)
    public RedirectView uploadOneFileHandlerPOST(HttpServletRequest request,
                                           @ModelAttribute("uploadForm") UploadForm uploadForm) {
        pathFromController = this.doUpload(request, uploadForm);
        RedirectView rv = new RedirectView();
        rv.setUrl("/parse");
        return rv;
    }

    private String doUpload(HttpServletRequest request, UploadForm uploadForm) {

        String uploadRootPath = request.getServletContext().getRealPath("upload");

        File uploadRootDir = new File(uploadRootPath);

        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        MultipartFile[] fileDatas = uploadForm.getFileDatas();

        List<File> uploadedFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();

        for (MultipartFile fileData : fileDatas) {

            String name = fileData.getOriginalFilename();

            if (name != null && name.length() > 0) {
                try {
                    File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                    stream.write(fileData.getBytes());
                    stream.close();

                    uploadedFiles.add(serverFile);
                } catch (Exception e) {
                    failedFiles.add(name);
                }
            }
        }
        return uploadedFiles.get(0).getAbsolutePath();
    }

}
