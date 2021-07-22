import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl } from '@angular/forms';
import { MatSlideToggleChange } from '@angular/material';

@Component({
  selector: 'app-decomposition-details',
  templateUrl: './decomposition-details.component.html',
  styleUrls: ['./decomposition-details.component.sass']
})
export class DecompositionDetailsComponent implements OnInit {

  private gitCheckoutPath:string;
  private useRepositoryAnalysis:boolean = true;
  private removeNoDependenciesClasses:boolean = true;
  private selectedAlgorithm:string;
  private nrServices:number;
  private allComplete: boolean = true;
  private repoStartDate:Date;
  private repoEndDate:Date;

  clusteringAlgorithms = [
    {value: 'GN', viewValue: 'Girvan Newman'},
    {value: 'HC', viewValue: 'Hierarchical Clustering'}
  ];

  temporalUnits = [
    {value: 'D', viewValue: 'Days'},
    {value: 'W', viewValue: 'Weeks'},
    {value: 'M', viewValue: 'Months'},
    {value: 'Y', viewValue: 'Years'}
  ];

  nameSimilarityOptions = {
    name: "Use naming similarity",
    completed: true,
    color: "primary",
    subtasks: [
      {name: "Class name", completed: true, color: "primary"},
      {name: "Package name", completed: true, color: "primary"}
    ]
  }
  
  @Output() decompositionPropertiesChanged = new EventEmitter();
  @Output() dateChange = new EventEmitter();
  
  constructor() { }

  ngOnInit() {
  }

  repoToggleChanged(event:MatSlideToggleChange){
    this.useRepositoryAnalysis = event.checked;
    this.verifyPropertiesFields();
  }

  noDependClassestoggleChanged(event:MatSlideToggleChange){
    this.removeNoDependenciesClasses = event.checked;
    this.verifyPropertiesFields();
  }

  decompositionPropertiesFilled(){
    let selectedAlgorithmFilled:Boolean = this.clusteringAlgorithms.find(({ value }) => value === this.selectedAlgorithm) !== null ? true : false;
    let nrServicesFilled:Boolean = this.nrServices !== undefined ? (this.nrServices > 0 ? true : false ) : false;

    return selectedAlgorithmFilled && nrServicesFilled;
  }

  repositoryPropertiesFilled(){
    let gitCheckoutPathFilled:Boolean = this.gitCheckoutPath !== undefined && this.gitCheckoutPath !== "" ? (/^[a-zA-Z]:\\(\w+\\)*\w*$/.test(this.gitCheckoutPath)) : false;
    let repoStartDate:Boolean = this.repoStartDate !== undefined ? true : false;
    let repoEndDate:Boolean = this.repoEndDate !== undefined ? true : false;

    return gitCheckoutPathFilled && repoStartDate && repoEndDate;
  }

  verifyPropertiesFields(){
    if(!this.useRepositoryAnalysis && this.decompositionPropertiesFilled() || (this.useRepositoryAnalysis && this.decompositionPropertiesFilled() && this.repositoryPropertiesFilled())){
      let repoStartDateSplitted = this.repoStartDate !== undefined ? this.repoStartDate.toDateString().split(" ") : "";
      let repoEndDateSplitted = this.repoEndDate !== undefined ? this.repoEndDate.toDateString().split(" ") : "";

      this.decompositionPropertiesChanged.emit(
        {
          stepState: "STEP2_READY",
          data: {
            systemModel:                    null, 
            clusteringAlgorithm:            this.selectedAlgorithm,
            nrClusters:                     this.nrServices,
            executeRepoAnalysis:            this.useRepositoryAnalysis,
            repoCheckoutPath:               !this.useRepositoryAnalysis ? null : this.gitCheckoutPath,
            repoStartDate:                  !this.useRepositoryAnalysis ? null : repoStartDateSplitted[1].toUpperCase() + " " + repoStartDateSplitted[2] + " " + repoStartDateSplitted[3],
            repoEndDate:                    !this.useRepositoryAnalysis ? null : repoEndDateSplitted[1].toUpperCase() + " " + repoEndDateSplitted[2] + " " + repoEndDateSplitted[3],
            useSimilarity:                  this.nameSimilarityOptions.subtasks[0].completed || this.nameSimilarityOptions.subtasks[1].completed,
	          className:                      this.nameSimilarityOptions.subtasks[0].completed,
	          packageName:                    this.nameSimilarityOptions.subtasks[1].completed,
            removeNoDependenciesClasses:    this.removeNoDependenciesClasses
          }
        }
      );
    }else{
      //verificar se tem ficheiro uploaded
      //se tiver emite "STEP2_READY"
      //se não emite
      this.decompositionPropertiesChanged.emit(
        {
          stepState: "STEP2_NOT_READY",
          data: null
        }
      );
    }
  }

  updateAllComplete() {
    this.allComplete = this.nameSimilarityOptions.subtasks != null && this.nameSimilarityOptions.subtasks.every(t => t.completed);
    this.verifyPropertiesFields();
  }

  someComplete(): boolean {
    if (this.nameSimilarityOptions.subtasks == null) {
      return false;
    }
    return this.nameSimilarityOptions.subtasks.filter(t => t.completed).length > 0 && !this.allComplete;
  }

  setAll(completed: boolean) {
    this.allComplete = completed;
    if (this.nameSimilarityOptions.subtasks == null) {
      return;
    }
    this.nameSimilarityOptions.subtasks.forEach(t => t.completed = completed);
    this.verifyPropertiesFields();
  }
}
