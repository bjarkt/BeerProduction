import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SubheaderComponent } from './subheader/subheader.component';
import {MatProgressBarModule, MatPaginatorModule, MatSnackBarModule, MatListModule, MatToolbarModule, MatDividerModule, MatButtonModule, MatTabsModule, MatTableModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatCardModule} from '@angular/material';
import { Routes, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';


@NgModule({
    declarations: [
        SubheaderComponent
    ],
    imports: [ 
        RouterModule,
        MatPaginatorModule,
        CommonModule,
        MatButtonModule,
        MatTabsModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCardModule,
        MatDividerModule,
        MatToolbarModule,
        MatListModule,
        FormsModule,
        MatSnackBarModule,
        MatProgressBarModule
    ],
    exports: [
        RouterModule,
        SubheaderComponent,
        MatPaginatorModule,
        MatButtonModule,
        MatTabsModule,
        MatTableModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCardModule,
        MatDividerModule,
        MatToolbarModule,
        MatListModule,
        FormsModule,
        MatSnackBarModule,
        MatProgressBarModule
    ],
    providers: []
  })
  export class SharedModule { }