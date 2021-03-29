import { Component } from '@angular/core';
import { FileManagementService } from '../../services/file-management.service'

@Component({
  selector: 'app-upload-file',
  templateUrl: './upload-file.component.html',
  styleUrls: ['./upload-file.component.sass']
})
export class UploadFileComponent{
  private file:File;

  constructor(
    private fileManagementService: FileManagementService
  ) {
    this.file = null;
  }

  fileSelection(files : File[]){
    if(files.length > 0){
      this.file = files[0];
    }
  }

  onSubmit(){
    this.fileManagementService.uploadFile(this.file)
      .subscribe(() => console.log('uploaded'));
  }
}
