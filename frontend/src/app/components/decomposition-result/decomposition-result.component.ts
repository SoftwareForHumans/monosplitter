import { Component, Input, OnChanges } from '@angular/core';

@Component({
  selector: 'app-decomposition-result',
  templateUrl: './decomposition-result.component.html',
  styleUrls: ['./decomposition-result.component.sass']
})
export class DecompositionResultComponent{

  @Input() decompositionResultData: any;
  constructor() { }

}
