import { Component, EventEmitter } from '@angular/core';
import { FormControl } from '@angular/forms';
import { MatStepper } from '@angular/material/stepper';
import { ProjectAnalysisService } from '../../services/project-analysis.service';
import { DecompositionService } from '../../services/decomposition.service';

@Component({
  selector: 'app-workflow-stepper',
  templateUrl: './workflow-stepper.component.html',
  styleUrls: ['./workflow-stepper.component.sass']
})
export class WorkflowStepperComponent {

  //STEPPER CONTROL
  uploadProjectStep = new FormControl()
  uploadProjectNextButtonState = false;
  decompositionDetailsStep = new FormControl()
  decompositionDetailsNextButtonState = false;
  decompositionStep = new FormControl();

  //SERVICE DATA
  private decompositionProperties:Object;

  //LOADING CONTROLLER
  private showLoadingSpinner:Boolean = false;

  decompositionResult:any;


  constructor(
    private projectAnalysisService: ProjectAnalysisService,
    private projectDecompositionService: DecompositionService
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
    switch(action.stepState){
      case "STEP1_READY":
        this.uploadProjectNextButtonState = true;
        break;
      case "STEP1_NOT_READY":
        this.activateStep1();
        this.uploadProjectNextButtonState = false;
        break;
      case "STEP2_READY":
        this.decompositionProperties = action.data;
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
    this.showLoadingSpinner = true;
    this.projectAnalysisService.staticAnalysis()
      .subscribe(data => this.executeStaticAnalysisServiceHandler(stepper, data));
  }

  executeStaticAnalysisServiceHandler(stepper : MatStepper, data: any){
    this.showLoadingSpinner = false;
    let self = this;

    setTimeout(function(){
      self.activateStep2()
      stepper.next();
      }, 250);
  }

  executeDecompositionService(stepper : MatStepper){
    this.showLoadingSpinner = true;
    this.projectDecompositionService.decomposeSystem(this.decompositionProperties)
      .subscribe(data => this.executeDecompositionServiceHandler(stepper, data));
  }

  executeDecompositionServiceHandler(stepper : MatStepper, data: any){
    this.showLoadingSpinner = false;
    let self = this;
    this.decompositionResult = data;
    setTimeout(function(){
      self.activateStep3()
      stepper.next();
    }, 250);
  }
}
