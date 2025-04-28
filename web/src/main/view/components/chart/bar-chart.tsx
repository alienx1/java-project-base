import { ChartTemplateConfig } from "@/config/chart-config";
import { Bar, BarChart, CartesianGrid, LabelList, LabelListProps, XAxis, XAxisProps, YAxis, YAxisProps } from "recharts"
import { Card, CardContent } from "../ui/card";
import { ChartContainer, ChartLegend, ChartLegendContent, ChartTooltip, ChartTooltipContent } from "../ui/chart";



export default function BarChartComponent({ datakey, chartData, chartDataKey, layout, chartConfig, isStacked = false, isLabel }: ChartTemplateConfig) {

    const configXAxis: Partial<XAxisProps> = layout === "horizontal"
        ? {
            dataKey: datakey?.toString(),
            tickLine: false,
            type: "category",
            tickMargin: 10,
            axisLine: false,
            tickFormatter: (value: string) => value.slice(0, 3),
        }
        : {
            type: "number",
            hide: true,
        };
    const configYAxis: Partial<YAxisProps> = {
        dataKey: datakey?.toString(),
        tickLine: false,
        type: "category",
        tickMargin: 10,
        axisLine: false,
        tickFormatter: (value) => value?.toString().slice(0, 3),
    };
    const configLabel: Partial<LabelListProps<any>> | undefined = isLabel?.label ? {
        position: isLabel.labelPosition,
        offset: 12,
        className: "fill-foreground",
        fontSize: 12,
    } : undefined;
    return (
        <Card>
            <CardContent>
                <ChartContainer config={chartConfig}>
                    <BarChart accessibilityLayer data={chartData} layout={layout}
                        margin={layout === "vertical" ? { left: -20 } : undefined}
                    >
                        <CartesianGrid vertical={layout === "vertical" ? true : false} horizontal={layout === "horizontal" ? true : false} />

                        <XAxis {...configXAxis} />

                        {layout === "vertical" && (<YAxis{...configYAxis} />)}
                        <ChartTooltip content={<ChartTooltipContent hideLabel />} />
                        <ChartLegend content={<ChartLegendContent />} />

                        {chartDataKey?.map((item: any) => (
                            <Bar
                                key={item.dataKey}
                                dataKey={item.dataKey}
                                fill={item.fill}
                                stackId={isStacked ? "stack" : undefined}
                                radius={layout === "horizontal" ? [4, 4, 0, 0] : [0, 4, 4, 0]}
                            >
                                {
                                    isLabel?.label && (<LabelList {...configLabel} />)
                                }

                            </Bar>
                        ))}
                    </BarChart>
                </ChartContainer>
            </CardContent>
        </Card>
    );

}