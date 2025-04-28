"use client";

import AreaChartComponent from "@/components/chart/area-chart";
import LayoutCard from "@/components/layout/card";
import TitlePage from "@/components/title-page";
import BarChartComponent from "@/components/chart/bar-chart";
import { ChartConfig } from "@/components/ui/chart";
import PieChartComponent from "@/components/chart/pie-chart";
import { LabelPosition } from "@/config/chart-config";
import { Button } from "@heroui/button";
import { Link } from "@heroui/link";

const chartData = [
    { month: "January", desktop: 186, mobile: 80 },
    { month: "February", desktop: 305, mobile: 200 },
    { month: "March", desktop: 237, mobile: 120 },
    { month: "April", desktop: 73, mobile: 190 },
    { month: "May", desktop: 209, mobile: 130 },
    { month: "June", desktop: 214, mobile: 140 },
]

const chartLabel = [
    { dataKey: "desktop", type: "natural", fill: "#DFFF00", fillOpacity: "0.8" },
    { dataKey: "mobile", type: "natural", fill: "#CCCCFF", fillOpacity: "0.8" }
]
const chartConfig = {
    desktop: {
        label: "Desktop",
        color: "#DFFF00",
    },
    mobile: {
        label: "Mobile",
        color: "#CCCCFF",
    },
} satisfies ChartConfig;

const isLabel: { label: boolean; labelPosition: LabelPosition } = {
    label: true,
    labelPosition: 'right'
};


const chartConfigPie = {
    label: {
        label: "Visitors",
    },
    chrome: {
        label: "Chrome",
        color: "hsl(var(--chart-1))",
    },
    safari: {
        label: "Safari",
        color: "hsl(var(--chart-2))",
    },
    firefox: {
        label: "Firefox",
        color: "hsl(var(--chart-3))",
    },
    edge: {
        label: "Edge",
        color: "hsl(var(--chart-4))",
    },
    other: {
        label: "Other",
        color: "hsl(var(--chart-5))",
    },
} satisfies ChartConfig

const chartDataPie = [
    { browser: "chrome", value: 275, fill: "var(--color-chrome)" },
    { browser: "safari", value: 200, fill: "var(--color-safari)" },
    { browser: "firefox", value: 287, fill: "var(--color-firefox)" },
    { browser: "edge", value: 173, fill: "var(--color-edge)" },
    { browser: "other", value: 190, fill: "var(--color-other)" },
]
function ContentChart() {
    return (
        <>
            <LayoutCard
                className="w-full "
                contentHeader={
                    <div className="flex gap-4 justify-end w-full">
                    <Button
                        showAnchorIcon
                        as={Link}
                        color="primary"
                        href="https://ui.shadcn.com/charts"
                        variant="solid"
                    >
                        docs chart
                    </Button>
                </div>

                }
                contentBody={(
                    <div className="flex flex-col gap-4 w-full">
                        <div className="flex flex-col md:flex-row gap-4">
                            <LayoutCard className="w-full md:max-w-md"
                                contentHeader={<h1>Area Chart</h1>}
                                contentBody={(
                                    <div className="w-full">
                                        <AreaChartComponent datakey="month" chartData={chartData} chartDataKey={chartLabel} chartConfig={chartConfig}  />
                                    </div>
                                )}
                            />

                            <LayoutCard className="w-full md:max-w-md"
                                contentHeader={<h1>Bar Chart Vertical</h1>}
                                contentBody={(
                                    <div className="w-full">
                                        <BarChartComponent datakey="month" chartData={chartData} chartDataKey={chartLabel} chartConfig={chartConfig} layout="vertical" isLabel={isLabel} />
                                    </div>
                                )}
                            />

                            <LayoutCard className="w-full md:max-w-md"
                                contentHeader={<h1>Pie Chart</h1>}
                                contentBody={(
                                    <div className="w-full">
                                        <PieChartComponent
                                            chartData={chartDataPie}
                                            chartDataKey="browser"
                                            chartConfig={chartConfigPie} />
                                    </div>
                                )}
                            />

                            <LayoutCard className="w-full md:max-w-md"
                                contentHeader={<h1>Bar Chart Horizontal</h1>}
                                contentBody={(
                                    <div className="w-full">
                                        <BarChartComponent datakey="month" chartData={chartData} chartDataKey={chartLabel} chartConfig={chartConfig} layout="horizontal" isStacked={true} />
                                    </div>
                                )}
                            />
                        </div>
                    </div>
                )}
            />

        </>
    );
}
export default function ChartDemo() {
    return (
        <>
            <TitlePage name="Chart" />
            <ContentChart />
        </>
    );
}