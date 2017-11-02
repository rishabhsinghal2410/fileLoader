package com.fileloader.controller;

import com.fileloader.models.FileStatus;
import com.fileloader.service.FileLoaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Controller
public class FileLoaderController {

    @Autowired
    private FileLoaderService service;

    @GetMapping("/")
    public String getAllFilesLoaded(Model model){
        model.addAttribute("files", service.getAllFiles());
        return "landingPage";
    }

    @GetMapping("/currencyDealCount")
    public String getAllCurrencyCountLoaded(Model model) throws ExecutionException, InterruptedException {
            model.addAttribute("currencyCounts", service.getAllCurrencyDealCount().stream().map(currencyDealCount -> currencyDealCount.toString()).collect(Collectors.toList()));
        return "currencyCount";
    }

    @GetMapping("/files/{filename:.+}")
    public String serveFile(@PathVariable String filename, Model model) {

        model.addAttribute("message", filename);
        model.addAttribute("deals", service.getAllDeals(filename).stream().map(deal -> deal.toString()).collect(Collectors.toList()));
        model.addAttribute("corruptDeals", service.getAllCorruptDeals(filename).stream().map(corruptDeal -> corruptDeal.toString()).collect(Collectors.toList()));
        return "fileDetails";
    }

    @PostMapping("/")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) throws IOException, ExecutionException, InterruptedException {
        List<FileStatus> filesStatus = service.getAllFiles();
        for (FileStatus fileStatus: filesStatus) {
            if(fileStatus.getFileName().equalsIgnoreCase(file.getOriginalFilename())){
                redirectAttributes.addFlashAttribute("message",
                        "File already exists!");
            }
        }
            service.load(file);
            redirectAttributes.addFlashAttribute("message",
                    "You successfully uploaded " + file.getOriginalFilename() + "!");
        redirectAttributes.addFlashAttribute("files", service.getAllFiles());
        return "redirect:/";
    }

    @ExceptionHandler(Exception.class)
    public String handleStorageFileNotFound(Exception exc, Model model) {
        model.addAttribute("message", "Exception Occurred while processing your request : " + exc.getMessage());
        return "error";
    }
}
