"use client"

import { ColorVariant, StyleVariant } from "@/components/layout/variant";
import { Card, CardBody, Tabs, Tab } from "@heroui/react";

export interface LayoutTabConfig {
    className?: any;
    color?: ColorVariant;
    disabled?: string[];
    style?: StyleVariant;
    tabs: TabData[];
}

export interface TabData {
    key: string;
    title: React.ReactNode;
    body: React.ReactNode;
}

export default function LayoutTab(config: LayoutTabConfig) {
    return (
        <Card className={config.className} radius="md">
            <CardBody className="overflow-hidden">
                <Tabs
                    aria-label="Options"
                    color={config.color}
                    disabledKeys={config.disabled}
                    variant={config.style}
                >
                    {config.tabs.map((x) => (
                        <Tab key={x.key} title={x.title}>
                            {x.body}
                        </Tab>
                    ))}
                </Tabs>
            </CardBody>
        </Card>
    );
}
