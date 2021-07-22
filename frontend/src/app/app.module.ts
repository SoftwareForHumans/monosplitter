import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { UploadFileComponent } from './components/upload-file/upload-file.component';
import { WorkflowStepperComponent } from './components/workflow-stepper/workflow-stepper.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatStepperModule, MatSnackBarModule, MatButtonModule, MatSlideToggleModule, MatTabsModule, MatSelectModule, MatInputModule, MatProgressSpinnerModule } from '@angular/material';
import { NavigationBarComponent } from './components/navigation-bar/navigation-bar.component';
import { DndDirective } from './directives/dnd.directive';
import { ProgressComponent } from './components/progress/progress.component';
import { DecompositionDetailsComponent } from './components/decomposition-details/decomposition-details.component';
import { DecompositionResultComponent } from './components/decomposition-result/decomposition-result.component';
import { MonolithClusters } from './components/monolith-clusters/monolith-clusters.component';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material';
import { MonolithClustersTextualComponent } from './components/monolith-clusters-textual/monolith-clusters-textual.component';
import { MatTreeModule } from '@angular/material/tree';
import { MatIconModule } from '@angular/material/icon';

@NgModule({
  declarations: [
    AppComponent,
    UploadFileComponent,
    WorkflowStepperComponent,
    NavigationBarComponent,
    DndDirective,
    ProgressComponent,
    DecompositionDetailsComponent,
    DecompositionResultComponent,
    MonolithClusters,
    MonolithClustersTextualComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    BrowserAnimationsModule,
    MatStepperModule,
    MatSnackBarModule,
    MatButtonModule,
    MatSlideToggleModule,
    MatTabsModule,
    MatSelectModule,
    MatInputModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTreeModule,
    MatIconModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
