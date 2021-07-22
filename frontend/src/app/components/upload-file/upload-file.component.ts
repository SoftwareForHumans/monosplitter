import { Component, EventEmitter, Input, Output, ViewChild, ElementRef } from '@angular/core';
import { MatSnackBar } from '@angular/material';
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
  @ViewChild('fileDropRef', { static: false }) inputElement: ElementRef;
  @Output() fileChanged = new EventEmitter();
  @Input() disableComponent = false;

  constructor(
    private fileManagementService: FileManagementService,
    private snackBar: MatSnackBar
  ) {
    this.file = null;
  }

  onFileDropped($event){
    if(!this.disableComponent){
      this.fileBrowseHandler($event);
    }
  }

  fileBrowseHandler(files : File[]){
    if(files.length > 0){
      let fileName = files[0].name.split('.');
      let fileType = fileName[fileName.length - 1];
      if(fileType.toUpperCase() === "JAR"){
        this.file = files[0];
        this.fileProgress = 0;
        this.progressBarState = "loading";
        this.deleteClassName = "unclick";
        this.executeFileUploadService();
      }else{
        this.snackBar.open(fileType.toUpperCase() + " is not a valid file type", null, {
          duration: 2000,
        });
      }
    }else{
      this.snackBar.open("Something went wrong... Upload project file again!", null, {
        duration: 2000,
      });
    }
  }

  executeFileUploadService(){
    this.progressAnimation();
    this.fileManagementService.uploadFile(this.file)
      .subscribe(data => this.executeFileUploadServiceHandler(data));
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

  executeFileUploadServiceHandler(data){
    if(data === null){
      this.uploadSuccess();
    }else{
      if(data.ok){
        this.uploadSuccess();
      }else{
        this.progressBarState = "error";
      }
    }
    this.deleteClassName = "click";
  }

  uploadSuccess(){
    this.fileProgress = 100;
    this.progressBarState = "success";
    this.fileChanged.emit(
      {
        stepState: "STEP1_READY"
      }
    );
  }

  deleteFile(){
    if(this.deleteClassName === "unclick"){
      return false;
    }else{
      this.file = null;
      this.inputElement.nativeElement.value = null;
      this.deleteClassName = "unclick";
      this.fileChanged.emit(
        {
          stepState: "STEP1_NOT_READY"
        }
      );
    }
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
