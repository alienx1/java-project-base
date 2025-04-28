import { ChartConfig } from "@/components/ui/chart"

export interface ChartTemplateConfig {
        datakey?: String; // example "month"
        chartData?: any[]; // x axis example  [{  { month: "January", desktop: 186, mobile: 80 }, ... } ]
        chartDataKey?: any; // y axis example  [{  { dataKey: "mobile", type: "natural", fill: "#CD5C5C", fillOpacity: "0.8" }, ... } ] and "desktop"
        chartConfig: ChartConfig; // example [{  desktop: {label: "Desktop",color: "#CD5C5C", ...},}]
        layout?: LayoutType;
        isStacked?: boolean;
        isLabel?: {
                label: boolean;
                labelPosition: LabelPosition;
        }
}

export type LayoutType = 'horizontal' | 'vertical' | 'centric' | 'radial';
export type LabelPosition = 'top' | 'left' | 'right' | 'bottom' | 'inside' | 'outside' | 'insideLeft' | 'insideRight' | 'insideTop' | 'insideBottom' | 'insideTopLeft' | 'insideBottomLeft' | 'insideTopRight' | 'insideBottomRight' | 'insideStart' | 'insideEnd' | 'end' | 'center' | 'centerTop' | 'centerBottom' | 'middle' | {
        x?: number;
        y?: number;
};
