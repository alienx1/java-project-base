'use client'

import { ContentFormArray } from "@/components/from/ContentFormArray";
import LayoutCard from "@/components/layout/card";
import TitlePage from "@/components/title-page";
import {
    ArrowUpTrayIcon,
    CurrencyDollarIcon,
    MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import {
    autocomplete,
    Autocomplete,
    AutocompleteItem,
    Button,
    Checkbox,
    CheckboxGroup,
    Code,
    DateInput,
    DatePicker,
    DateRangePicker,
    Input,
    NumberInput,
    Radio,
    RadioGroup,
    Select,
    SelectItem,
    Switch,
    Textarea,
} from "@heroui/react";
import { parseDate } from "@internationalized/date";
import React from "react";
import {
    useForm,
    Controller,
    FormProvider,
    useFieldArray,
    useFormContext,
} from "react-hook-form";
import { date } from "zod";

function ContentTemplateFormArray({ index }: { index: number }) {
    const { control } = useFormContext();
    return (
        <>
            <Controller
                name={`name.${index}.firstname`}
                control={control}
                render={({ field }) => (
                    <Input {...field} label="First Name" placeholder="Firstname" className="w-full" />
                )}
            />
            <Controller
                name={`name.${index}.lastname`}
                control={control}
                render={({ field }) => (
                    <Input {...field} label="Last Name" placeholder="Lastname" className="w-full" />
                )}
            />
        </>
    );
}

function ContentForm() {
    const methods = useForm({
        defaultValues: {
            search: undefined,
            text: undefined,
            email: undefined,
            number: undefined,
            price: undefined,
            date: undefined,
            dateRange: {
                start: undefined,
                end: undefined,
            },
            checkbox: false,
            checkboxGroup: [],
            multilineselect: [],
            radiogroup: undefined,
            select: undefined,
            autocomplete: undefined,
            textarea: undefined,
            file: undefined,
            name: [],
        },
    });

    const { control, handleSubmit, reset, watch } = methods;
    const { fields, append, remove } = useFieldArray({ control, name: "name" });
    const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>, onChange: (value: any) => void) => {
        const file = event.target.files?.[0]; // Get the file from the input
        if (file) {

            const reader = new FileReader();
            reader.onloadend = () => {
                const buffer = reader.result as ArrayBuffer;
                const byteArray = Array.from(new Uint8Array(buffer));
                onChange(byteArray); 
            };
            reader.onerror = () => {
                console.error("Error reading the file.");
                onChange(null);
            };
            reader.readAsArrayBuffer(file); 
            onChange(null);
        }
    };
    


    const onSubmit = (data: any) => console.log(data);
    const onReset = () => reset();

    const animals = [
        { key: "cat", label: "Cat" },
        { key: "dog", label: "Dog" },
        { key: "lion", label: "Lion" },
        { key: "elephant", label: "Elephant" },
    ];

    return (
        <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)} onReset={onReset}>
                <LayoutCard
                    className="w-full"
                    contentHeader={
                        <div className="flex flex-col gap-4 w-full">

                            <div className="flex flex-col md:flex-row gap-4">
                                <Controller
                                    name="search"
                                    control={control}
                                    render={({ field }) => (
                                        <Input {...field}
                                            labelPlacement="outside"
                                            placeholder="Search Bar"
                                            className="w-full md:w-5/12" startContent={
                                                <MagnifyingGlassIcon className="h-5 w-5 mr-3 text-muted-foreground group-hover:text-foreground" />
                                            }
                                        />
                                    )}
                                />
                            </div>
                            <div className="flex flex-col md:flex-row gap-4">
                                <Controller
                                    name="text"
                                    control={control}
                                    render={({ field }) => (
                                        <Input {...field} label="Text" placeholder="Text" className="w-full md:max-w-md" />
                                    )}
                                />
                                <Controller
                                    name="email"
                                    control={control}
                                    render={({ field }) => (
                                        <Input {...field} label="Email" type="email" placeholder="Email" className="w-full md:max-w-md" />
                                    )}
                                />
                                <Controller
                                    name="number"
                                    control={control}
                                    render={({ field }) => (
                                        <NumberInput {...field} label="Number" className="w-full md:max-w-md" />
                                    )}
                                />
                                <Controller
                                    name="price"
                                    control={control}
                                    render={({ field }) => (
                                        <NumberInput
                                            {...field}
                                            label="Price"
                                            className="w-full md:max-w-md"
                                            startContent={<CurrencyDollarIcon className="h-5 w-5 mr-3" />}
                                        />
                                    )}
                                />
                            </div>

                            <div className="flex flex-col md:flex-row gap-4">
                                <Controller
                                    name="date"
                                    control={control}
                                    render={({ field }) => (
                                        <DatePicker {...field} label="Date" className="w-full md:max-w-md" />
                                    )}
                                />
                                <Controller
                                    name="dateRange"
                                    control={control}
                                    render={({ field: { onChange, value } }) => (
                                        <DateRangePicker
                                            className="w-full md:max-w-md"
                                            label="Date Range Picker"
                                            startName="startDate"
                                            endName="endDate"
                                            value={value}
                                            onChange={onChange}
                                        />
                                    )}
                                />
                                <Controller
                                    name="select"
                                    control={control}
                                    render={({ field }) => (
                                        <Select {...field} label="Select" placeholder="Select" className="w-full md:max-w-md"  >
                                            {animals.map((animal) => (
                                                <SelectItem key={animal.key}>{animal.label}</SelectItem>
                                            ))}
                                        </Select>
                                    )}
                                />
                                <Controller
                                    name="multilineselect"
                                    control={control}
                                    render={({ field }) => {
                                        const value = Array.isArray(field.value)
                                            ? field.value
                                            : typeof field.value === 'string'
                                                ? field.value.split(',')
                                                : [];

                                        return (
                                            <Select
                                                label="Multi Line Select"
                                                placeholder="Select"
                                                className="w-full md:max-w-md"
                                                selectionMode="multiple"
                                                isMultiline
                                                selectedKeys={new Set(value)}
                                                onSelectionChange={(keys) => {
                                                    field.onChange(Array.from(keys));
                                                }}
                                            >
                                                {animals.map((animal) => (
                                                    <SelectItem key={animal.key}>{animal.label}</SelectItem>
                                                ))}
                                            </Select>
                                        );
                                    }}
                                />

                            </div>
                            <div className="flex flex-col md:flex-row gap-4">
                                <Controller
                                    name="radiogroup"
                                    control={control}
                                    render={({ field }) => (
                                        <RadioGroup
                                            {...field}
                                            label="Radio Group"
                                            className="w-full md:max-w-md"
                                            orientation="horizontal"
                                        >
                                            {animals.map((animal) => (
                                                <Radio key={animal.key} value={animal.key}>
                                                    {animal.label}
                                                </Radio>
                                            ))}
                                        </RadioGroup>
                                    )}
                                />
                                <Controller
                                    name="checkboxGroup"
                                    control={control}
                                    render={({ field }) => (
                                        <CheckboxGroup
                                            {...field}
                                            label="Checkbox Group"
                                            className="w-full md:max-w-md"
                                            orientation="horizontal"
                                        >
                                            {animals.map((animal) => (
                                                <Checkbox key={animal.key} value={animal.key}>
                                                    {animal.label}
                                                </Checkbox>
                                            ))}
                                        </CheckboxGroup>
                                    )}
                                />
                                <Controller
                                    name="autocomplete"
                                    control={control}
                                    render={({ field }) => (
                                        <Autocomplete
                                            label="Autocomplete"
                                            placeholder="Select"
                                            className="w-full md:max-w-md"
                                            selectedKey={field.value}
                                            onSelectionChange={(key) => field.onChange(key)}
                                        >
                                            {animals.map((animal) => (
                                                <AutocompleteItem key={animal.key}>{animal.label}</AutocompleteItem>
                                            ))}
                                        </Autocomplete>
                                    )}
                                />
                                <Controller
                                    name="checkbox"
                                    control={control}
                                    render={({ field }) => (
                                        <Checkbox isSelected={field.value} onValueChange={field.onChange} className="w-full md:max-w-md">
                                            Checkbox
                                        </Checkbox>
                                    )}
                                />
                            </div>
                            <div className="flex flex-col md:flex-row gap-4">
                                <Controller
                                    name="textarea"
                                    control={control}
                                    render={({ field }) => (
                                        <Textarea {...field} label="Textarea" placeholder="Enter description" className="w-full md:max-w-md" />
                                    )}
                                />
                                <Controller
                                    name="file"
                                    control={control}
                                    render={({ field }) => (
                                        <Input
                                            label="File Upload"
                                            {...field}
                                            type="file"
                                            accept="*"
                                            className="w-full md:max-w-lg"
                                            radius="md"
                                            startContent={
                                                <ArrowUpTrayIcon className="h-5 w-5 mr-3 text-muted-foreground group-hover:text-foreground" />
                                            }
                                            description="Select a file to upload (any file type accepted)"
                                            onChange={async (event) => {
                                                const selectedFile = event.target.files?.[0];

                                                if (selectedFile) {
                                                    await handleFileChange(event, field.onChange);
                                                } else {
                                                    field.onChange(null); 
                                                }
                                            }}
                                        />
                                    )}
                                />


                            </div>
                            <div className="flex flex-col md:flex-row gap-4">
                                <LayoutCard
                                    className="w-full"
                                    contentBody={
                                        <>
                                            {fields.map((_, index) => (
                                                <div key={index} className="flex gap-4 items-end py-1">
                                                    <ContentTemplateFormArray index={index} />
                                                    <Button onClick={() => remove(index)} type="button" color="danger">
                                                        Remove
                                                    </Button>
                                                </div>
                                            ))}
                                            <div className="flex gap-4 py-1">
                                                <Button onClick={() => append({ firstname: "", lastname: "" })} type="button" className="w-full">
                                                    Add Name
                                                </Button>
                                            </div>
                                        </>
                                    }
                                />
                            </div>
                            <div className="flex gap-4">
                                <Button type="submit" color="primary">Submit</Button>
                                <Button type="reset" color="secondary">Reset</Button>
                            </div>
                        </div>
                    }
                    contentBody={
                        <Code size="md" className="w-full whitespace-pre-wrap">
                            {JSON.stringify(watch(), null, 2)}
                        </Code>
                    }
                />
            </form>
        </FormProvider>
    );
}

export default function DemoInput() {
    return (
        <>
            <TitlePage name="Input" />
            <ContentForm />
        </>
    );
} 
