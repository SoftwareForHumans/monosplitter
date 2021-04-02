import { Component } from '@angular/core';
import { FileManagementService } from '../../services/file-management.service'

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.sass']
})
export class UploadFileComponent{
  private file:File = null
  fileProgress = 0;
  progressBarState = "loading";
  deleteClassName = "unclick";

  constructor(
    private fileManagementService: FileManagementService
  ) {
    this.file = null;
  }

  onFileDropped($event){
    this.fileBrowseHandler($event);
  }

  fileBrowseHandler(files : File[]){
    if(files.length > 0){
      this.file = files[0];
      this.fileProgress = 0;
    }
    this.progressBarState = "loading";
    this.deleteClassName = "unclick";
    this.executeFileUploadService();
  }

  progressAnimation(){
    setTimeout(() => {
      let progressInterval = setInterval(() => {
        if (this.progressBarState === "success" || this.progressBarState === "error") {
          clearInterval(progressInterval);
        } else {
          this.fileProgress += 2;
        }
      }, 200);
    }, 1000);
  }

  deleteFile(){
    if(this.deleteClassName === "unclick"){
      return false;
    }else{
      this.file = null;
      this.deleteClassName = "unclick";
    }
  }

  executeFileUploadService(){
    this.progressAnimation();
    this.fileManagementService.uploadFile(this.file)
      .subscribe(data => this.executeFileUploadServiceHandler(data));
  }

  executeFileUploadServiceHandler(data){
    if(data === null){
      this.fileProgress = 100;
      this.progressBarState = "success";
    }else{
      if(data.ok){
        this.fileProgress = 100;
        this.progressBarState = "success";
      }else{
        this.progressBarState = "error";
      }
    }
    this.deleteClassName = "click";
  }

  formatBytes(bytes, decimals = 2) {
    if (bytes === 0) {
      return "0 Bytes";
    }
    const k = 1024;
    const dm = decimals <= 0 ? 0 : decimals;
    const sizes = ["Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB"];
    const i = Math.floor(Math.log(bytes) / Math.log(k));
    return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + " " + sizes[i];
  }
}
