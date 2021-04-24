import { Component } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { ProjectAnalysisService } from '../../services/project-analysis.service';

@Component({
  selector: 'app-workflow-stepper',
  templateUrl: './workflow-stepper.component.html',
  styleUrls: ['./workflow-stepper.component.sass']
})
export class WorkflowStepperComponent {

  uploadProjectStep = new FormControl()
  uploadProjectNextButtonState = false;
  decompositionDetailsStep = new FormControl()
  decompositionDetailsNextButtonState = false;
  decompositionStep = new FormControl();

  constructor(
    private projectAnalysisService: ProjectAnalysisService,
  ) {
    this.setState(this.uploadProjectStep, true);
    this.setState(this.decompositionDetailsStep, false);
    this.setState(this.decompositionStep, false);
  }

  setState(control:FormControl, state:boolean){
    if (state) {
      control.setErrors({ "required": true })
    } else {
      control.reset()
    }
  }

  disableEveryStep(){
    this.setState(this.uploadProjectStep, false);
    this.setState(this.decompositionDetailsStep, false);
    this.setState(this.decompositionStep, false);
  }

  activateStep1(){
    this.disableEveryStep();
    this.setState(this.uploadProjectStep, true);
  }

  activateStep2(){
    this.disableEveryStep();
    this.setState(this.decompositionDetailsStep, true);
  }

  activateStep3(){
    this.disableEveryStep();
    this.setState(this.decompositionStep, true);
  }

  handleSteppersState(action:any){
    console.log("estou aqui")
    switch(action){
      case "STEP1_READY":
        this.uploadProjectNextButtonState = true;
        break;
      case "STEP1_NOT_READY":
        this.activateStep1();
        this.uploadProjectNextButtonState = false;
        break;
      case "STEP2_READY":
        this.activateStep3();
        this.decompositionDetailsNextButtonState = true;
        break;
      case "STEP2_NOT_READY":
        this.activateStep2();
        this.decompositionDetailsNextButtonState = false;
      default:
        break;
    }
  }

  executeStaticAnalysisService(stepper : MatStepper){
    this.projectAnalysisService.staticAnalysis()
      .subscribe(data => this.executeStaticAnalysisServiceHandler(stepper, data));
  }

  executeStaticAnalysisServiceHandler(stepper : MatStepper, data: any){
    this.activateStep2()
    stepper.next();
  }
}
