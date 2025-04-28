import { ChartTemplateConfig } from "@/config/chart-config";
import { ChartContainer, ChartLegend, ChartLegendContent, ChartTooltip, ChartTooltipContent } from "../ui/chart";
import { Area, AreaChart, CartesianGrid, XAxis } from "recharts"
import { Card, CardContent } from "@/components/ui/card"



export default function AreaChartComponent({ datakey, chartData, chartDataKey ,  chartConfig }: ChartTemplateConfig) {
    return (
        <Card>
            <CardContent>
                <ChartContainer config={chartConfig}>
                    <AreaChart
                        accessibilityLayer
                        data={chartData}
                        margin={{
                            left: 12,
                            right: 12,
                        }}
                    >
                        <CartesianGrid vertical={false} />
                        <XAxis
                            dataKey={datakey?.toString()}
                            tickLine={false}
                            axisLine={false}
                            tickMargin={8}
                            tickFormatter={(value) => value.slice(0, 3)}
                        />
                        <ChartTooltip
                            cursor={false}
                            content={<ChartTooltipContent indicator="dot" />}
                        />
                        {chartDataKey?.map((item:any) => (
                            <Area
                                key={item.dataKey}
                                dataKey={item.dataKey}
                                type="natural"
                                fill={item.fill}
                                fillOpacity={0.4}
                                stroke={item.fill}
                                stackId="a"
                            />
                        ))}
                        <ChartLegend content={<ChartLegendContent />} />
                    </AreaChart>
                </ChartContainer>
            </CardContent>
        </Card>

    );
}