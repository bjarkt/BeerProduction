import { MeasurementsStatistics } from "./MeasurementsStatistics";
import { BatchStatistics } from "./BatchStatistics";


export interface PlantStatistics {
    measurementsStatistics: MeasurementsStatistics;
    batchStatistics: BatchStatistics;
    
}