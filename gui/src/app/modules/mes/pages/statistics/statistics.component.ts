import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { DataService } from 'src/app/shared/services/data.service';
import { MesDataService } from 'src/app/shared/services/mesData.service';
import { Batch } from 'src/app/shared/models/Batch';
import { MeasurementsStatistics } from 'src/app/shared/models/MeasurementsStatistics';
import { PlantStatistics } from 'src/app/shared/models/PlanStatistics';

@Component({
  selector: 'app-statistics',
  templateUrl: './statistics.component.html',
  styleUrls: ['./statistics.component.scss']
})
export class StatisticsComponent implements OnInit {
  plantStatistics: PlantStatistics;
  days: number = 1;

  constructor(private data: MesDataService) { }

  ngOnInit() {
    this.getStatistics();
  }

  public getStatistics() {
    this.data.viewStatistics(this.days).subscribe (result => {
      this.plantStatistics = result
    }) 
  }

}



