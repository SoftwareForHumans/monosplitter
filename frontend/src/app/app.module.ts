import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { HttpClientModule }    from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UploadFileComponent } from './components/upload-file/upload-file.component';
import { WorkflowStepperComponent } from './components/workflow-stepper/workflow-stepper.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatStepperModule, MatSnackBarModule, MatButtonModule, MatSlideToggleModule, MatTabsModule } from '@angular/material';
import { NavigationBarComponent } from './components/navigation-bar/navigation-bar.component';
import { DndDirective } from './directives/dnd.directive';
import { ProgressComponent } from './components/progress/progress.component';
import { DecompositionDetailsComponent } from './components/decomposition-details/decomposition-details.component';
import { DecompositionResultComponent } from './components/decomposition-result/decomposition-result.component';

@NgModule({
  declarations: [
    AppComponent,
    UploadFileComponent,
    WorkflowStepperComponent,
    NavigationBarComponent,
    DndDirective,
    ProgressComponent,
    DecompositionDetailsComponent,
    DecompositionResultComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    BrowserAnimationsModule,
    MatStepperModule, 
    MatSnackBarModule, 
    MatButtonModule,
    MatSlideToggleModule,
    MatTabsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
