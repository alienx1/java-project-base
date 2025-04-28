import { Card } from "@heroui/react";
import { PieChart, Pie, Label } from "recharts";
import { CardContent } from "../ui/card";
import { ChartContainer, ChartLegend, ChartLegendContent, ChartTooltip, ChartTooltipContent } from "../ui/chart";
import { ChartTemplateConfig } from "@/config/chart-config";
import React from "react";



export default function PieChartComponent({ chartData, chartDataKey, chartConfig }: ChartTemplateConfig) {
    const totalValue = (chartData ?? []).reduce(
        (acc: number, item: any) => acc + (item.value || 0),
        0
    );
    return (
        <>
            <Card >
                <CardContent >
                    <ChartContainer
                        config={chartConfig}
                    >
                        <PieChart>
                            <ChartTooltip
                                cursor={false}
                                content={<ChartTooltipContent hideLabel />}
                            />
                            <Pie
                                data={chartData}
                                dataKey="value"
                                nameKey={chartDataKey?.toString()}
                                innerRadius={50}
                                strokeWidth={5}
                            >
                                <Label
                                    content={({ viewBox }) => {
                                        if (viewBox && "cx" in viewBox && "cy" in viewBox) {
                                            return (
                                                <text
                                                    x={viewBox.cx}
                                                    y={viewBox.cy}
                                                    textAnchor="middle"
                                                    dominantBaseline="middle"
                                                >
                                                    <tspan
                                                        x={viewBox.cx}
                                                        y={viewBox.cy}
                                                        className="fill-foreground text-3xl font-bold"
                                                    >
                                                        {totalValue.toLocaleString()}
                                                    </tspan>
                                                    <tspan
                                                        x={viewBox.cx}
                                                        y={(viewBox.cy || 0) + 24}
                                                        className="fill-muted-foreground"
                                                    >
                                                        {chartDataKey?.toString()}
                                                    </tspan>
                                                </text>
                                            )
                                        }
                                    }}
                                />
                            </Pie>
                            <ChartLegend content={<ChartLegendContent />} />
                        </PieChart>
                    </ChartContainer>
                </CardContent>
            </Card>
        </>
    );
}