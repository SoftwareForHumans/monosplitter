import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatSlideToggleChange } from '@angular/material';

@Component({
  selector: 'app-decomposition-details',
  templateUrl: './decomposition-details.component.html',
  styleUrls: ['./decomposition-details.component.sass']
})
export class DecompositionDetailsComponent implements OnInit {

  private useDynamicAnalysis = true;
  @Output() decompositionPropertiesChanged = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  toggleChanged(event:MatSlideToggleChange){
    this.useDynamicAnalysis = event.checked;
    this.verifyPropertiesFields();
  }

  verifyPropertiesFields(){
    if(!this.useDynamicAnalysis){
      this.decompositionPropertiesChanged.emit("STEP2_READY");
    }else{
      //verificar se tem ficheiro uploaded
      //se tiver emite "STEP2_READY"
      //se não emite
      this.decompositionPropertiesChanged.emit("STEP2_NOT_READY");
    }
  }

}
