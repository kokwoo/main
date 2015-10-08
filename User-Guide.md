# About
Tempo is a simple tool that allows users to plan and manage their daily schedules using single line commands. With Tempo, you can now add, edit, search, delete, and view events and tasks - all with a single line command!

# Table of Contents
- [Feature Details](#feature-details)
  - [Basic Operations](#basic-operations)
  - [Input Formats](#input-formats)
  - [Manage Your Events](#manage-your-events)
  - [Manage Your Tasks](#manage-your-tasks)
  - [Display Calendar](#display-calendar)
  - [Search](#search)
- [Cheatsheet](#cheatsheet)

# Feature Details
## Basic Operations
To quickly list available commands, use the command `help`. 

To undo your previous operation, use the command `undo`. The most recently made change will then be reverted.

To obtain the id of your events or tasks, use any view or search command listed below. The ids of your events or tasks are unique for each item and necessary for their updating and deletion.

To exit Tempo, use the command `exit`.

## Input Formats
The input format for dates across all commands should follow this syntax: `DD/MM/YYYY`. If the date input does not exist, you will be alerted and the previous operation with the invalid date input will not be carried out.

The input format for time across all commands should follow the 24-hour format. For example, 2:01PM should be input as `1401` and 4:30AM should be input as `0430`.

## Manage Your Events
To add an event, use the following command. The key `add` can be replaced by `create` or `new`.

    add <name> from <start date> at <start time> to <end date> at <end time>

If the event is to be repeated, simply append `repeat:<frequency>` to the above command, where the `<frequency>` can be `daily`, `weekly`, `monthly` or `yearly`.

    add <name> from <start date> at <start time> to <end date> at <end time> repeat:<frequency>

To update an event, use the following command. The key `update` can be replaced by `edit` or `change`. If there are more than one fields to be modified, append as many `<field name>:<new value>` as required using the delimeter `; `. 

 - `update <id> <field name>:<new value>`
 - `update <id> <field name>:<new value>; <field name>:<new value>`

To repeat an event, use the following command. The possible choices for the frequency of the events are `daily`, `weekly`, `monthly` or `yearly`.

    repeat <id> <frequency> until <end date>

To delete an event, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete <id>

## Manage Your Tasks
To add a task, use one of the following commands. The key `add` can be replaced by `create` or `new`. Use the second command if a deadline is to be specified.

 - `add <name>`
 - `add <name> due <date>`

To update a task, use the following command. The key `update` can be replaced by `edit` or `change`. If there are more than one fields to be modified, append as many `<field name>:<new value>` as required using the delimeter `; `. 

 - `update <id> <field name>:<new value>`
 - `update <id> <field name>:<new value>; <field name>:<new value>`

To mark a task as done, use the following command. The key `done` can be replaced by `finished` or `completed`.

    done <id>

To delete a task, use the following command. The key `delete` can be replaced by `cancel` or `remove`. If you delete a recurring event, all instances of the event will be deleted.

    delete <id>

## Display Calendar
To view the events and tasks that are happening or due today, use the following command. The key `view` can be replaced by `display`.

    view today

To view all events and tasks, use the following command. The key `view` can be replaced by `display`.

    view calendar

To view only your events, use the following commands. The key `view` can be replaced by `display`.

 - `view upcoming events`
 - `view events`

To view only your tasks, use the following command. The key `view` can be replaced by `display`.

 - `view undone tasks`
 - `view missed tasks`
 - `view all tasks`


## Search 
To search for an event or task, use the following commands. At least part of the event or task name must be keyed in the `<keywords>` field. The key `search` can be replaced by `find`.

    search <keywords>

# Cheatsheet
Command | `<key>` Variations| Description
--------| --------------| ---------------
`help` | N.A. | List available commands
`<key> <name> from <start date> at <start time> to <end date> at <end time>` | `add`, `create`, `new` | Add event
`<key> <name> from <start date> at <start time> to <end date> at <end time> repeat:<frequency of occurrence>` | `add`, `create`, `new` | Add recurring event
`<key> <name>` | `add`, `create`, `new` | Add task with no deadline
`<key> <name> due <date>` | `add`, `create`, `new` | Add task with deadline
`<key> <id> <field name>:<new value>` | `update`, `edit`, `change` | Update single field of event/task
`<key> <id> <field name>:<new value>; <field name>:<new value>` | `update`, `edit`, `change` | Update multiple fields of event/task
`<key> <id>` | `done`, `finished`, `completed` | Mark task as done 
`repeat <id> <frequency> until <end date>` | N.A. | Repeat event (daily, weekly, monthly or yearly)
`<key> <id>` | `delete`, `remove`, `cancel` | Delete event or task
`<key> today` | `view`, `display` | View today's events and tasks
`<key> calendar` | `view`, `display` | View all events and tasks
`<key> upcoming events` | `view`, `display` | View upcoming events
`<key> events` | `view`, `display` | View all events
`<key> undone tasks` | `view`, `display` | View undone tasks
`<key> missed tasks` | `view`, `display` | View missed events
`<key> all tasks` | `view`, `display` | View all tasks
`<key> <keywords>` | `search`, `find` | Search for an event or task
`undo` | N.A. | Undo previous command
`exit` | N.A. | Exit Tempo
