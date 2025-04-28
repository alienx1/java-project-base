"use client";

import {
    HomeIcon,
    UserIcon,
    CogIcon,
    ChevronDownIcon,
    ChevronRightIcon,
    XMarkIcon,
    ShieldExclamationIcon,
    InformationCircleIcon,
} from "@heroicons/react/24/outline";
import Link from "next/link";
import clsx from "clsx";
import { useState } from "react";

const navItems = [
    {
        name: "Demo",
        icon: ShieldExclamationIcon,
        menu: [
            { name: "Layout", href: "/demo/layout", icon: InformationCircleIcon },
            { name: "Input", href: "/demo/input", icon: InformationCircleIcon },
            { name: "Table", href: "/demo/table", icon: InformationCircleIcon },
            { name: "Chart", href: "/demo/chart", icon: InformationCircleIcon },

        ],
    },
];

export default function Sidebar({
    isOpen,
    setIsOpen,
}: {
    isOpen: boolean;
    setIsOpen: (open: boolean) => void;
}) {
    const [openSections, setOpenSections] = useState<Record<string, boolean>>({});

    const toggleSection = (sectionName: string) => {
        setOpenSections((prev) => ({
            ...prev,
            [sectionName]: !prev[sectionName],
        }));
    };

    return (
        <>
            <aside
                className={clsx(
                    "fixed inset-y-0 left-0 z-40 w-64 bg-background text-foreground border-r border-border transform transition-transform duration-300 ease-in-out",
                    {
                        "-translate-x-full": !isOpen,
                        "translate-x-0": isOpen,
                    }
                )}
            >
                <div className="p-6 text-xl font-semibold flex items-center justify-between">
                    <span>MyApp</span>
                    <button
                        onClick={() => setIsOpen(false)}
                        className="text-muted-foreground hover:text-foreground"
                    >
                        <XMarkIcon className="h-5 w-5" />
                    </button>
                </div>

                <nav className="mt-4 px-4">
                    {navItems.map((section) => {
                        const isOpen = openSections[section.name];

                        return (
                            <div key={section.name} className="mb-4">
                                <button
                                    className="w-full flex items-center justify-between text-sm font-semibold text-muted-foreground hover:text-foreground mb-2"
                                    onClick={() => toggleSection(section.name)}
                                >   <div className="w-full flex">
                                    <section.icon className="h-5 w-5 mr-3 text-muted-foreground group-hover:text-foreground" />
                                    <span>{section.name}</span>
                                    </div>
                                    {isOpen ? (
                                        <ChevronDownIcon className="w-5 h-5" />
                                    ) : (
                                        <ChevronRightIcon className="w-5 h-5" />
                                    )}
                                </button>

                                {isOpen && (
                                    <div className="space-y-1">
                                        {section.menu.map((item) => (
                                            <Link
                                                key={item.name}
                                                href={item.href}
                                                onClick={() => setIsOpen(false)}
                                                className="group flex items-center px-3 py-2 rounded-md text-sm font-medium hover:bg-muted transition-colors"
                                            >
                                                <item.icon className="h-5 w-5 mr-3 text-muted-foreground group-hover:text-foreground" />
                                                <span>{item.name}</span>
                                            </Link>
                                        ))}
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </nav>
            </aside>

            {isOpen && (
                <div
                    className="fixed inset-0 z-30 bg-black/40"
                    onClick={() => setIsOpen(false)}
                />
            )}
        </>
    );
}
